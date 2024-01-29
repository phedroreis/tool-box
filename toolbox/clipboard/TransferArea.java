package toolbox.clipboard;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.awt.datatransfer.Clipboard;

/*******************************************************************************
* Por meio desta classe e possivel copiar texto e codigo HTML para  o clipboard
* do sistema. Tambem fornece metodos estaticos para obter o texto e/ou o codigo
* HTML que tiver sido copiado para o clipboard.
*******************************************************************************/
public final class TransferArea implements Transferable {
 
    private String html;
    private String plainText;
    private static final DataFlavor[] FLAVORS = new DataFlavor[2];
    private static final Clipboard CLIPBOARD = 
        Toolkit.getDefaultToolkit().getSystemClipboard();
    
    static {
        FLAVORS[0] = DataFlavor.stringFlavor;
        FLAVORS[1] = DataFlavor.allHtmlFlavor; 
    }
    
    /***************************************************************************
    * Insere no clipboard um texto puro e a versao HTML que o gerou.
    *
    * @param plainText O texto.
    *
    * @param html O codigo HTML.
    ***************************************************************************/
    public static void setContents(final String plainText, final String html) {
        
        CLIPBOARD.setContents(new TransferArea(plainText, html), null);
        
    }//setContents
    
    /***************************************************************************
    * Obtem o codigo HTML copiado para o clipboard. Se houver.
    *
    * @return O codigo HTML copiado para o clipboard. Se houver.
    *
    * @throws IOException Em caso de erro de IO.
    *
    * @throws UnsupportedFlavorException Se nao houver codigo HTML no clipboard.
    ***************************************************************************/
    public static String getHtml() 
        throws 
            IOException, 
            UnsupportedFlavorException {
        
        return CLIPBOARD.getData(DataFlavor.allHtmlFlavor).toString();
        
    }//getHtml
    
    /***************************************************************************
    * Obtem um texto puro se existir no clipboard.
    *
    * @throws IOException Em caso de erro de IO.
    *
    * @throws UnsupportedFlavorException Se nao houver texto no clipboard.
    ***************************************************************************/
    public static String getPlainText() 
        throws 
            IOException, 
            UnsupportedFlavorException {
        
        return CLIPBOARD.getData(DataFlavor.stringFlavor).toString();
        
    }//getPlainText
    
    /***************************************************************************
    * Construtor.
    *
    * @param plainText Um texto plain para ser copiado para o clipboard.
    *
    * @param html Um codigo HTML para ser copiado para o clipboard.
    ***************************************************************************/
    public TransferArea(final String plainText, final String html) {
        
        this.html = html;
        this.plainText = plainText;
        
    }//construtor
    
    /***************************************************************************
    * Retorna todos os <b><i>DataFlavors</i></b> suportados.
    *
    * @return Um array com os tipos de <b><i>DataFlavors</i></b>
    * suportados.
    ***************************************************************************/
    public DataFlavor[] getTransferDataFlavors() {
        
        return FLAVORS;
        
    }//getTransferDataFlavors
    
    /***************************************************************************
    * Retorna se esse tipo de flavor e suportado.
    *
    * @param flavor O tipo de flavor.
    *
    * @return <code>true</code> se o flavor e suportado.
    ***************************************************************************/
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
        
        for (DataFlavor dataFlavor : FLAVORS) 
            if (flavor == dataFlavor) return true;
            
        return false;
        
    }//isDataFlavorSupported
    
    /***************************************************************************
    * Retorna um objeto que representa o dado requisitado.
    *
    * @param flavor Transfere um dado deste tipo se existir no
    * clipboard.
    *
    * @return O objeto representando o dado.
    *
    * @throws UnsupportedFlavorException No caso de ser requisitado um flavor
    * nao suportado por este objeto.
    ***************************************************************************/
    public Object getTransferData(final DataFlavor flavor) 
        throws UnsupportedFlavorException {

        if (!String.class.equals(flavor.getRepresentationClass())) 
            throw new UnsupportedFlavorException(flavor);
  
        if (flavor == DataFlavor.stringFlavor) return plainText;
    
        return html;
        
    }//getTransferData
    
}//classe TransferArea
