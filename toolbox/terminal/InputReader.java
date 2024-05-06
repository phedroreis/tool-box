package toolbox.terminal;

import java.util.Scanner;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

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
    private final Scanner inputReader;
    private final PrintWriter console;
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    private static String msg$4;
    private static String msg$5;
    private static String msg$6;
    
    static {
        try {
            
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.InputReader", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1");
            msg$2 = rb.getString("msg$2");
            msg$3 = rb.getString("msg$3");
            msg$4 = rb.getString("msg$4");
            msg$5 = rb.getString("msg$5");
            msg$6 = rb.getString("msg$6");  
        
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            
            msg$1 = "Charset for inputs:";
            msg$2 = " (Y/n)";
            msg$3 = "Y/y";
            msg$4 = "y";
            msg$5 = "No";
            msg$6 = "N/n";   
        }    
    }


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
    * @throws IllegalArgumentException Se o charset for <code>null</code>.
    * 
    * @throws IllegalCharsetNameException Se o nome do charset nao obedecer as 
    * regras para nomeacao de charsets.
    * 
    * @throws UnsupportedCharsetException Se o charset nao for suportado pela JVM
    * em execucao.
    ***************************************************************************/
    public InputReader(final String charset) 
        throws IllegalArgumentException, IllegalCharsetNameException, UnsupportedCharsetException {
        
        console = new PrintWriter(System.out, true, Charset.forName(charset));
        
        console.printf("%n%s %s%n", msg$1, charset);
        
        inputReader = new Scanner(System.in, charset);
       
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
    * @param label Uma pergunta que so possa ser respondida com sim ou nao.
    *
    * @param isYesDefaultOption Se sim for a opçao default.
    ***************************************************************************/
    public void setPrompt(
        final String label,
        final boolean isYesDefaultOption
    ){
        
        this.label = label + msg$2;
        
        if (isYesDefaultOption) {
            
            enterOptionLabel = msg$3;
            defaultOption = msg$4;
        }
        else {
            
            enterOptionLabel = msg$5;
            defaultOption = msg$6;       
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
            
            console.printf("%n%s:%n", label);
            console.printf("[ENTER = %s] >", enterOptionLabel);
            
            try {           
          
                input = inputReader.nextLine(); 
                
                if (input.isBlank()) return defaultOption;
                
                input = parser.parse(input);
            }
            catch (IllegalArgumentException e) {
                
                console.printf("%n%s%n", e.getMessage());
                err = true;             
            }
            
        } while (err);
        
        return input;
        
    }//readInput 
    
}//classe InputReader
