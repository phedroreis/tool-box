package toolbox.terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/*******************************************************************************
* Classe responsável por processar entradas de usuário pelo terminal.
*
* <p>Um objeto desta classe exibe um prompt para entrada de dados no terminal
* e valida a entrada por meio de um objeto {@link InputParser InputParser}
* passado ao seu construtor.</p>
*
* @see toolbox.InputParser
* @since 1.0 - 2 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class InputReader {
    
private String label;
private String enterOptionLabel;
private String defaultOption;
private InputParser parser;
private final BufferedReader inputReader;
private final PrintStream console;

    /***************************************************************************
    * Construtor.
    *
    * @param charset O encoding que o terminal irá usar para ler os dados.
    *
    * <p>Este encoding é definido pelo sistema. As opções possíveis são:</p>
    *
    *<ul>
    *<li>iso-8859-1
    *<li>us-ascii
    *<li>utf16
    *<li>utf_16be
    *<li>utf_16le
    *<li>utf8
    *</ul>
    *
    * @throws UnsupportedEncodingException Se o código do charset nao
    * corresponder a um dos listados acima ou se a instancia da JVM
    * executando nao suportar o charset passado como argumento.
    ***************************************************************************/
    public InputReader(final String charset) 
        throws UnsupportedEncodingException {
        
        console = new PrintStream(System.out, true, charset);
        
        console.println("\nLendo entradas em " + charset);
        
        inputReader = 
            new BufferedReader(new InputStreamReader(System.in, charset));
       
    }//construtor

    /***************************************************************************
    * Configura o prompt de entrada de dados.
    *
    * @param label Uma mensagem especifica que tipo de dado é esperado.
    *
    * @param enterOptionLabel Uma mensagem para informar ao usuário qual o valor
    * default para esta entrada.
    *
    * @param defaultOption O valor default que deve ser retornado se o usuario
    * teclar ENTER ou fornecer uma entrada em branco.
    *
    * @param parser Um objeto de uma classe que estenda a classe abstrata
    * {@link InputParser InputParser} e que sera responsável por validar
    * esta entrada.
    ***************************************************************************/
    public void setPrompt(
        final String label,
        final String enterOptionLabel,
        final String defaultOption,
        final InputParser parser
    ){
        
        this.label = label;
        this.enterOptionLabel = enterOptionLabel;
        this.defaultOption = defaultOption;
        this.parser = parser;      
      
    }//setPrompt
    
    /***************************************************************************
    * Para quando o prompt for exibir uma opçao de escolha do tipo sim ou
    * nao.
    *
    * @param label Uma string indicando ao usuario que dado entrar.
    *
    * @param isYesDefaultOption Se sim for a opçao default.
    ***************************************************************************/
    public void setPrompt(
        final String label,
        final boolean isYesDefaultOption
    ){
        
        this.label = label + "? (S/n)";
        
        if (isYesDefaultOption) {
            
            enterOptionLabel = "Sim";
            defaultOption = "s";
        }
        else {
            
            enterOptionLabel = "N\u00e3o";
            defaultOption = "n";       
        }
        this.parser = new InputParserYesOrNo();      
      
    }//setPrompt


    /***************************************************************************
    * O metodo apresenta a mensagem adequada especificando o tipo de entrada,
    * le a entrada e retorna um valor validado.
    *
    * @return Uma entrada validada
    *
    ***************************************************************************/
    public String readInput() {
        
        boolean err;
        String input = null;
        
        do {
            err = false;
            
            console.println('\n' + label + ':');
            console.print("[ENTER = " + enterOptionLabel + "] >");
            
            try {           
          
                input = inputReader.readLine(); 
                
                if (input.isBlank()) return defaultOption;
                
                input = parser.parse(input);
            }
            catch (IllegalArgumentException e) {
                
                console.println('\n' + e.getMessage());
                err = true;             
            }
            catch (IOException e) {
                
                System.err.println('\n' + e.getMessage());
                System.exit(1);
            }
            
        } while (err);
        
        return input;
        
    }//readInput   
    
}//classe InputReader
