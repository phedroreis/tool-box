package toolbox.terminal;
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
    /***************************************************************************
    * Valida a entrada apenas se o usuario entrou com Sim
    * ou Nao.
    *
    * <p>Exemplos de entradas validas:</p>
    *
    * <ul>
    * <li>Sim</li>
    * <li>Nao</li>
    * <li>n</li>
    * <li>sim</li>
    * <li>siM</li>
    * <li>NÃO</li>
    * <li>SIM</li>
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
        
        String parsedInput = input;
        
        switch (parsedInput.toLowerCase()) {
            case "s":
            case "sim":
                return "s";
            case "n":
            case "n\u00e3o":
            case "nao":
                return "n";
            default:
                throw new IllegalArgumentException("Entrada inv\u00e1lida!");
        }
           
    }//parse
    
}//classe InputParserYesOrNo
