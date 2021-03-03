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

public class GerarComandosPmd extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		List<IProject> projetos = Arrays.asList(workspaceRoot.getProjects());
		List<String> comandos = new ArrayList<String>();
		
		projetos.forEach((projeto) -> {
			if (projeto.isOpen()) {
				StringBuilder sb = new StringBuilder();
				sb.append("pmd.bat -d \"");
				sb.append(projeto.getLocation().toString().replace("/", "\\"));
				sb.append("\" -f text -R custom-rules.xml > \"C:/Users/Ramon/Resultados/NaoNormalizados/");
				sb.append(projeto.getName());
				sb.append(".pmd\"");
				
				comandos.add(sb.toString());
			}
		});
		
		try {
			String nomeArquivo = "comandos PMD.txt";
			Files.write(Paths.get("C:/Users/Ramon/Mestrado/Experimento/Oxygen/Comandos" + "/" + nomeArquivo), comandos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Sucesso",
				"Geração de comandos do PMD realizada com sucesso.");
		return null;
	}}
