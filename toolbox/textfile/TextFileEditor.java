package toolbox.textfile;

/*******************************************************************************
* Uma classe abstrata que especifica o método pra fazer edições em um arquivo
* tipo texto.
*
* <p>Um objeto TextFileEditor (instância de uma classe que estenda esta) deve
* ser passado a um dos metodos edit da classe 
* {@link TextFileHandler TextFileHandler}
* para que sua implementação do método abstrato desta classe se encarregue de
* fazer edições nas Strings localizadas, elegíveis para edição.</p>
*
*
* @since 1.0 - 15 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public abstract class TextFileEditor {
    
    /***************************************************************************
    * Uma implementação deste método deve ser chamada pelos métodos edit da
    * classe {@link TextFileHandler TextFileHandler} para cada substring 
    * elegível para edição que for localizada.
    *
    * <p>A implementação deve analisar a <b><i>String</i></b> que foi recebida
    * pelo parâmetro <b><i>match</i></b>, realizar as edições necessárias e
    * retornar uma string que seja a versão editada de <b><i>match</i></b>.
    * Esta string retornada substituirá a string <b><i>match</i></b> que foi
    * localizada. Ou então <code>null</code>, se o método decidir que a
    * substring original não deve ser modificada.</p>
    *
    * @param match Uma substring do arquivo lido por um objeto 
    * {@link TextFileHandler TextFileHandler}
    * e que foi localizada para edição, devendo ser substituída por uma outra
    * string que seja uma edição desta. Caberá a uma implementação deste método
    * realizar tais edições.
    *
    * @return A versão modificada da string <b><i>match</i></b> ou
    * <code>null</code> se a string não deve ser modificada.
    ***************************************************************************/
    public abstract String edit(final String match);
    
}//classe TextFileEditor
