package br.ufba.pesquisa.ramon.plugin.handlers;

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

public class SelecionarProjetosComModelos extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject[] projects = workspaceRoot.getProjects();
		
		try {
			for (IProject project : projects) {
				System.out.println(project.getName());
				filtrarProjetosEcore(project);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void filtrarProjetosEcore(IProject project) throws CoreException {
		
		try {
			System.out.println(project.getParent());
			if (!project.isOpen()) {
//				project.open(null);
				return;
			}
			
			boolean existeArquivosEcore = existeArquivosECore(project);
			
			if (!existeArquivosEcore) {
				project.close(null);
				project.delete(false, false, null);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private boolean existeArquivosECore(IContainer container) throws CoreException {
		
		IResource [] members = container.members();
		IResource member;
		IFile file;
		
		int i = 0;
		boolean encontrou = false;
		
		while (!encontrou) {
			
			if (i < members.length) {
				member = members[i];
				if (member instanceof IContainer) {
					encontrou = existeArquivosECore((IContainer) member);
				} else if (member instanceof IFile) {
					file = (IFile) member;
					if (Objects.nonNull(file) && Objects.nonNull(file.getFileExtension())) {
						encontrou = "ecore".contentEquals(file.getFileExtension()); 
					}
				}
				
			} else {
				return false;
			}
			i++;
		}
		
		return encontrou;

	}

}
