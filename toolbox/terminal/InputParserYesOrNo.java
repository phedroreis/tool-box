package toolbox.terminal;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/*******************************************************************************
* Estende a classe {@link InputParser InputParser} para
* criar um validador de entradas do tipo Sim ou Nao.
* Para quando o usuario deve escolher apenas entre estas
* duas opçoes.
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
* @see toolbox.terminal.InputParser
*******************************************************************************/
public class InputParserYesOrNo extends InputParser{
    
    private static String msg$1;
    private static String msg$2;
    private static String msg$3;
    
    static {
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.InputParserYesOrNo", toolbox.locale.Localization.getLocale());
            msg$1 = rb.getString("msg$1"); 
            msg$2 = rb.getString("msg$2");
            msg$3 = rb.getString("msg$3");
            
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            
            msg$1 = "y";        
            msg$2 = "n";
            msg$3 = "Invalid input!"; 
        }
    }
   
    /***************************************************************************
    * Valida a entrada apenas se o usuario entrou com Sim
    * ou Nao.
    *
    * <p>Exemplos de entradas validas:</p>
    *
    * <ul>
    * <li>S</li>
    * <li>N</li>
    * <li>n</li>
    * <li>s</li>
    * </ul>
    *
    * @param input Passa a entrada que o usuario digitou no prompt
    * para ser validada.
    *
    * @return A propria opcao passada no argumento <b><i>input</i></b>,
    * caso a entrada seja validada.
    *
    * <p>Se a entrada nao for validade, este metodo nao retornara valor
    * pois uma exceçao IllegalArgumentException sera lancada.</p>
    *
    * @throws IllegalArgumentException Se a entrada nao corresponde a
    * uma opçao sim ou nao, esta exceçao informa a mensagem de erro
    * "Entrada invalida!" que devera ser exibida ao usuario pelo
    * metodo chamador {@link InputReader#readInput() readInput}.
    ***************************************************************************/
    @Override
    public String parse(final String input) 
        throws IllegalArgumentException {
        
        String parsedInput = input.toLowerCase();
        
        if (parsedInput.equals(msg$1) || parsedInput.equals(msg$2)) return parsedInput;
             
        throw new IllegalArgumentException(msg$3);
   
    }//parse
    
}//classe InputParserYesOrNo
