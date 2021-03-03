package br.ufba.pesquisa.ramon.plugin.dialogs;

import java.util.Objects;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SelecionarArquivoDestinoDialog extends TitleAreaDialog {
	
	private String diretorioDestino;
	private String nomeArquivo;
	
	private Text txtSelecaoDiretorio;
	private Text txtNomeArquivo;
	
	public SelecionarArquivoDestinoDialog(Shell parentShell, String nomeDiretorioPadrao, String nomeArquivoPadrao) {
		super(parentShell);
		
		this.diretorioDestino = nomeDiretorioPadrao;
		this.nomeArquivo = nomeArquivoPadrao;
	}
	
	@Override
    public void create() {
        super.create();
        setTitle("Selecão de arquivo destino");
        setMessage("Selecione onde ficará o arquivo com os comandos necessários para executar a ferramenta nos projetos abertos do workspace.", IMessageProvider.INFORMATION);
    }
	
	@Override
    protected Control createDialogArea(Composite parent) {
        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(3, false);
        container.setLayout(layout);

        createSelecaoDiretorio(container);
        createSelecaoNomeArquivo(container);

        return area;
    }
	
	private void createSelecaoDiretorio(Composite container) {
        Label labelSelecaoDir = new Label(container, SWT.NONE);
        labelSelecaoDir.setText("Diretório de destino");

        GridData dataSelecaoDiretorio = new GridData();
        dataSelecaoDiretorio.grabExcessHorizontalSpace = true;
        dataSelecaoDiretorio.horizontalAlignment = GridData.FILL;

        txtSelecaoDiretorio = new Text(container, SWT.BORDER);
        txtSelecaoDiretorio.setLayoutData(dataSelecaoDiretorio);
        
        Button button = new Button(container, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
        button.setText("Selecionar");
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
            	fd.setText("Selecione diretório de destino");
            	String selected = fd.open();
            	if (Objects.nonNull(selected)) {
            		setDiretorioDestino(selected);
            	}
            }
        });
    }
	
	private void createSelecaoNomeArquivo(Composite container) {
        Label lbtLastName = new Label(container, SWT.NONE);
        lbtLastName.setText("Last Name");

        GridData dataNomeArquivo = new GridData();
        dataNomeArquivo.grabExcessHorizontalSpace = true;
        dataNomeArquivo.horizontalAlignment = GridData.FILL;
        txtNomeArquivo = new Text(container, SWT.BORDER);
        txtNomeArquivo.setLayoutData(dataNomeArquivo);
    }

	public String getDiretorioDestino() {
		return diretorioDestino;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public void setDiretorioDestino(String diretorioDestino) {
		this.diretorioDestino = diretorioDestino;
	}
}
