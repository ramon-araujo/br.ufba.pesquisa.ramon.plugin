package br.ufba.pesquisa.ramon.plugin.handlers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class GerarComandosCheckstyle extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
//		SelecionarArquivoDestinoDialog dialog = new SelecionarArquivoDestinoDialog(window.getShell(), "", "comandos checkstyle.txt");
//		dialog.create();
//		if (dialog.open() == Window.OK) {
//			System.out.println(dialog.getDiretorioDestino());
//			System.out.println(dialog.getNomeArquivo());
//		}
		
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		List<IProject> projetos = Arrays.asList(workspaceRoot.getProjects());
		List<String> comandos = new ArrayList<String>();
		
		projetos.forEach((projeto) -> {
			if (projeto.isOpen()) {
				StringBuilder sb = new StringBuilder();
				sb.append("java com.puppycrawl.tools.checkstyle.Main -c sun_checks.xml \"");
				sb.append(projeto.getLocation().toString().replace("/", "\\"));
				sb.append("\" > \"C:/Users/Ramon/Resultados/NaoNormalizados/");
				sb.append(projeto.getName());
				sb.append(".checkstyle\"");
				
				comandos.add(sb.toString());
			}
		});
		
		try {
			String nomeArquivo = "comandos checkstyle.txt";
			Files.write(Paths.get("C:/Users/Ramon/Mestrado/Experimento/Oxygen/Comandos" + "/" + nomeArquivo), comandos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MessageDialog.openInformation(
				window.getShell(),
				"Sucesso",
				"Geração de comandos do PMD realizada com sucesso.");
		
		return null;
	}

}
