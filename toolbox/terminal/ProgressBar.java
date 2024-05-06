package toolbox.terminal;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

/*******************************************************************************
* Implementa uma barra de progresso simples no terminal.
*
* <p>Devido ao fato de que uma aplicação Java não tem controle sobre o
* posicionamento do cursor em terminais de sistemas Windows, implementar uma
* barra de progresso está sujeito a algumas limitações. A principal delas é que
* enquanto um objeto desta classe estiver escrevendo na barra de progresso,
* <b>o programa não poderá efetuar nenhuma outra saída de texto para o terminal.
* </b></p>
*
* @since 1.0 - 14 de janeiro de 2024
* @version 1.0
* @author Pedro Reis
*******************************************************************************/
public final class ProgressBar {
    
    private final int total;
    private final int barLength;
    private final int stepsPerDot;
    private int countSteps;
    private static String exceptionMsg$1;
    private static String exceptionMsg$2;
    private static String exceptionMsg$3;
    
    static {
        try {
            ResourceBundle rb = 
                ResourceBundle.getBundle("toolbox.properties.ProgressBar", toolbox.locale.Localization.getLocale());
            exceptionMsg$1 = rb.getString("exception_msg$1");
            exceptionMsg$2 = rb.getString("exception_msg$2");
            exceptionMsg$3 = rb.getString("exception_msg$3");
        
        } catch (NullPointerException | MissingResourceException | ClassCastException e) {
            
            exceptionMsg$1 = "The process must have at least one step";
            exceptionMsg$2 = "Bar length < 1";
            exceptionMsg$3 = "Step's overflow";            

        } 
    }
    
    /***************************************************************************
    * Construtor.
    *
    * @param total O total de passos que serão executados para a realização do
    * processo.
    *
    * @param barLength A largura máxima (em caracteres), que a barra poderá ter.
    *
    * <p>Esta largura poderá ser ajustada para um tamanho menor</p>
    *
    * @throws IllegalArgumentException Se <b><i>total</i></b> menor que 1 ou 
    * <b><i>barLength</i></b> menor que 1.
    ***************************************************************************/
    public ProgressBar(final int total, int barLength) throws IllegalArgumentException {
        
        if (total < 1) throw new IllegalArgumentException(exceptionMsg$1);
        if (barLength < 1) throw new IllegalArgumentException(exceptionMsg$2);
        
        barLength++;
        
        int steps;  

        do {

        } while ((total % --barLength) >= (steps = total / barLength)); 
        
        this.total = total;
        this.barLength = barLength;
        stepsPerDot = steps;
        countSteps = 0;
        
    }//construtor
    
    /***************************************************************************
    * Exibe a barra no terminal.
    ***************************************************************************/
    public void showBar() {
        
        System.out.printf("%n0%%|%s|100%%%n   ", " ".repeat(barLength));
        
    }//showBar
    
    /***************************************************************************
    * Deve ser chamado a cada passo executado do processo.
    * 
    * @throws IndexOutOfBoundsException se este metodo for chamado mais vezes que
    * o total de passos passado como argumento no construtor da classe. 
    ***************************************************************************/
    public void increment() throws IndexOutOfBoundsException {
        
        if (++countSteps > total) throw new IndexOutOfBoundsException(exceptionMsg$3);
        
        if (countSteps % stepsPerDot == 0) System.out.print(".");  
        
    }//increment
    
}//classe ProgressBar
