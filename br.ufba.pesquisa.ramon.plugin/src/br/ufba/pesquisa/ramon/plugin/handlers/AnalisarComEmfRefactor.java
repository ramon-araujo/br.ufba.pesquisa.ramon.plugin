package br.ufba.pesquisa.ramon.plugin.handlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.refactor.smells.runtime.managers.RuntimeManager;

public class AnalisarComEmfRefactor extends AbstractHandler {
	
	private BufferedWriter writer;
	private int counter = 1;


	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try {
			writer = new BufferedWriter(new FileWriter("arquivos-projeto.txt"));
		
			List<IFile> arquivos = encontrarArquivosEcore();
			
			for (IFile arquivo : arquivos) {
				ResourceSet resourceSet = new ResourceSetImpl();
				URI uri = URI.createURI(arquivo.getFullPath().toString());
				Resource resource = resourceSet.getResource(uri, true);
				
				EList<EObject> contents = resource.getContents();
				if (!contents.isEmpty()) {
					EObject rootPackage = resource.getContents().get(0);
					System.out.println(rootPackage);
					
					IFile selectedFile = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(arquivo.getFullPath().toString());
					IProject project = selectedFile.getProject();
					RuntimeManager.findConfiguredModelSmells(project, rootPackage, selectedFile);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	private List<IFile> encontrarArquivosEcore() throws IOException {
		
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		
		List<IFile> arquivos = new ArrayList<>();
		
		for (IProject iProject : projects) {
			writer.append("Nome do projeto: " + iProject.getName());
			writer.newLine();
			if (iProject.isOpen()) {
				arquivos.addAll(listarArquivosEcore(iProject));
			}
		}
		return arquivos;
	}
	
	private List<IFile> listarArquivosEcore(IContainer container) throws IOException {

		List<IFile> arquivosEncontrados = new ArrayList<>();
		try {
			IResource [] members = container.members();
			IFile file;
			
			for (IResource iResource : members) {
				if (iResource instanceof IContainer) {
					arquivosEncontrados.addAll(listarArquivosEcore((IContainer) iResource));
				} else if (iResource instanceof IFile) {
					file = (IFile) iResource;
					if (ehUmArquivoEcore(file)) {
						writer.append("                          " + counter++ + ". " + file.getName());
						writer.newLine();
						arquivosEncontrados.add(file);
					}
				}
			}
			
		} catch (CoreException e) {
			e.printStackTrace();
		}
	
		return arquivosEncontrados;
	}
	
	private boolean ehUmArquivoEcore(IFile file) {
		return Objects.nonNull(file) && Objects.nonNull(file.getFileExtension()) && file.getFileExtension().equals("ecore");
	}

}
