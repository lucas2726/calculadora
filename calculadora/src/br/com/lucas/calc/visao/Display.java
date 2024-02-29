package br.com.lucas.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.lucas.calc.modelo.Memoria;
import br.com.lucas.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display extends JPanel implements MemoriaObservador{
	
	private final JLabel label;

	public Display() {
		Memoria.getInstacia().adicionarObservador(this);
		                      
		setBackground(new Color(46, 49, 50));
		label = new JLabel(Memoria.getInstacia().getTextoAtual());
		label.setForeground(Color.WHITE); 
		label.setFont(new Font("courier", Font.PLAIN, 30)); //Para definir o tipo da fonte, tamanho e estilo
		
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25)); //Para mudar a posicao do texto e 
		
		add(label);
	}

	@Override
	public void valorAlterado(String novoValor) {
		label.setText(novoValor);
		
	}
	
}
