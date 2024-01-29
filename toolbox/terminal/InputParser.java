package toolbox.terminal;

/*******************************************************************************
* Uma classe abstrata que especifica um método validador de dados para um
* objeto da classe {@link InputReader InputReader}.
*
* <p>Um objeto da classe InputReader permite entrada de dados customizada pelo
* terminal. Os dados entrados podem ser de qualquer tipo e finalidade,
* portanto um validador específico também deve ser fornecido.</p>
*
* <p>Este validador deve ser um objeto de uma classe que estenda InputParser e
* implemente o método abstrato {@link parse(String) parse} de acordo com a
* seguinte especificação:</p>
*
* <p>Quando o usuário confirmar um dado de entrada digitado no prompt fornecido
* por um objeto InputReader, o método parse será chamado para checar a
* validação deste dado, que será passado ao método parse na forma de String,
* exatamente como foi digitado no terminal.</p>
*
* <p>O método {@link parse(String) parse} pode entao testar se o dado eh válido
* ou não e, caso não seja, deve lancar uma
* {@link java.lang.IllegalArgumentException IllegalArgumentException} com a
* mensagem de erro apropriada. Mas caso o dado seja validado, o método deve
* somente retornar a String que lhe foi passada como argumento, sem nenhuma
* modificação. Sinalizando assim para o
* {@link InputReader#readInput()  método chamador} que a entrada foi validada.
* </p>
*
* <p>Exemplo de uso:</p>
*
* <p>Suponha que um programa solicite que o usuário digite uma cor primária
* (amarelo, vermelho ou azul). Um validador customizado deverah se certificar
* que apenas uma destas cores foi digitada, e não nenhum outro tipo de entrada.
* </p>
*
* <p>Uma classe para implementar este validador poderia ser:</p>
*
* <pre><code>
public class ParseColors extends InputParser {

{@literal @Override}
public abstract String parse(final String input)
throws IllegalArgumentException {

switch (input.toLowerCase()) {
case "amarelo":
case "vermelho":
case "azul":
return input;
default:
throw new IllegalArgumentException("Dado inválido!");
}

}//parse

}//classe ParseColors
* </code></pre>
*
* E, ao criar uma instância da classe {@link InputReader InputReader}
* (ou seja, um objeto do tipo InputReader), então uma instância desta classe
* ParseColors seria passada ao construtor do objeto tipo InputReader.
*
* @see toolbox.InputReader
* @since 1.0 - 2 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public abstract class InputParser {
    
    /***************************************************************************
    * O método deve realizar a validacao de uma entrada de usuário para um
    * objeto da classe {@link InputReader InputReader}.
    *
    * @param input A entrada digitada pelo usuário
    *
    * @return A propria entrada, se validada.
    *
    * @throws IllegalArgumentException Uma implementação deste método deve
    * lancar uma IllegalArgumentException para toda entrada invalida, com a
    * mensagem de erro adequada. Se a entrada invalida causar outro tipo de
    * exceção, esta deve ser capturada e o bloco <code>catch</code>
    * correspondente deve entao lançar uma <code>IllegalArgumentException</code>
    * com a mensagem de erro apropriada.
    ***************************************************************************/
     public abstract String parse(final String input) 
        throws IllegalArgumentException;
    
}//class InputParser
