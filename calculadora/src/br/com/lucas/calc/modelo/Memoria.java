package br.com.lucas.calc.modelo;

import java.util.ArrayList;
import java.util.List;

public class Memoria {
	
	private enum TipoComando {
		//Define os possíveis tipos de comandos que a calculadora pode recebe
		ZERAR, SINAL, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA; 
	}
	
	//Instância única da classe Memoria (Singleton pattern).
	private static final Memoria instacia = new Memoria(); 
	
	//Lista de observadores (listeners) para notificar sobre mudanças.
	private final List<MemoriaObservador> observadores = new ArrayList<>(); 

	
	private TipoComando ultimaOperacao = null;//Armazena o tipo da última operação realizada.
	private boolean substituir = false; //Indica se o próximo número substituirá o atual.
	private String textoAtual = ""; //Representa o texto atual na calculadora.
	private String textoBuffer = ""; //Armazena um texto temporário durante operações.
  
	private Memoria() {
		
	}

	public static Memoria getInstacia() { //Retorna a instancia
		return instacia;
	}
	
	public void adicionarObservador(MemoriaObservador observador) { // Adiciona um observador à lista.
		observadores.add(observador);
	}
	
	public String getTextoAtual() { //Retorna o texto atual (ou "0" se estiver vazio).

		return textoAtual.isEmpty() ? "0" : textoAtual;
	}
	
	public void processarComando(String texto) { 
		//Recebe um comando (botão pressionado) e executa a lógica correspondente.

		TipoComando tipoComando = detectarTipoComando(texto);
		
		if(tipoComando == null) {
			return;
		} else if(tipoComando == TipoComando.ZERAR) {
			textoAtual = "";
		    textoBuffer = "";
		    ultimaOperacao = null;
	        substituir = false;
		} else if(tipoComando == TipoComando.SINAL && textoAtual.contains("-")) {
			textoAtual = "-" + textoAtual.substring(1);
		} else if(tipoComando == TipoComando.SINAL && !textoAtual.contains("-")) {
			textoAtual = "-" + textoAtual;
		} else if(tipoComando == TipoComando.NUMERO 
				|| tipoComando == TipoComando.VIRGULA) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir = false;
		} else {
			substituir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = textoAtual;
			ultimaOperacao = tipoComando;
		}
		
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() { 
		//Realiza a operação aritmética com base nos valores do buffer e atual.

		if(ultimaOperacao == null 
				|| ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		
		double numeroBuffer =
				Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual =
				Double.parseDouble(textoAtual.replace(",", "."));
		
		double resultado = 0;
		
		if(ultimaOperacao == TipoComando.SOMA) {
			resultado = numeroBuffer + numeroAtual;
		} else if(ultimaOperacao == TipoComando.SUB) {
			resultado = numeroBuffer - numeroAtual;
		} else if(ultimaOperacao == TipoComando.MULT) {
			resultado = numeroBuffer * numeroAtual;
		} else if(ultimaOperacao == TipoComando.DIV) {
			resultado = numeroBuffer / numeroAtual;
		}
		
		String texto = Double.toString(resultado).replace(".", ",");
		boolean inteiro = texto.endsWith(",0");
		return inteiro ? texto.replace(",0", "") : texto;
	}

	private TipoComando detectarTipoComando(String texto) { 
		//analisa o tipo de comando com base no texto recebido.

		if(textoAtual.isEmpty() && texto == "0") {
			return null;
		}
		
		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			if("AC".equals(texto)) {
				return TipoComando.ZERAR;
			} else if("/".equals(texto)) {
				return TipoComando.DIV;
			} else if("*".equals(texto)) {
				return TipoComando.MULT;
			} else if("+".equals(texto)) {
				return TipoComando.SOMA;
			} else if("-".equals(texto)) {   
				return TipoComando.SUB;
			} else if("=".equals(texto)) {
				return TipoComando.IGUAL;
			} else if("±".equals(texto)) {
				return TipoComando.SINAL;
			} else if(",".equals(texto) 
					&& !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;
			}
		}
		return null;
		
	}
	
}
