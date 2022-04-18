package makeblastdb;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.node.port.PortObject;

/**
 * cette classe contient des outils de conversion entre URI et URIDataCell ou URIPortObject
 */

public class URIUtil{

	public static Class<?> URIContent;
	public static Class<?> URIDataCell;
	private static Class<?> URIPortObject;
	

    public static URI URIFromDataCell(Object leURIDataCell) throws Exception {
		//recupération du URIContent
		Class<?> URIDataCell = leURIDataCell.getClass();
		Method getURIContent = URIDataCell.getMethod("getURIContent", new Class<?>[] {});
        Object leURIContent = getURIContent.invoke(leURIDataCell, new Object[] {});
        
        //recuperation du URI from URIContent
        Class<?> URIContent = leURIContent.getClass();
        Method getURI = URIContent.getMethod("getURI", new Class<?>[] {});
        
        ///sauvegarde les classe pour les utiliser à la fin du noeud
       URIUtil.URIDataCell = URIDataCell;
       URIUtil.URIContent = URIContent;
        
        
        return (URI) getURI.invoke(leURIContent, new Object[] {});
	}
	
	

	/// a quick and dirty method to cast a URI to an DataCell.
	public static DataCell DataCellFromURI(URI leURI) throws Exception {
		
		Constructor<?> Curicontent = URIUtil.URIContent.getConstructor(new Class<?>[] {URI.class, String.class});
		Object leURIContent = Curicontent.newInstance(leURI, "");

		Constructor<?> Curidatacell = URIUtil.URIDataCell.getConstructor(new Class<?>[] {URIUtil.URIContent});
		
		return (DataCell) Curidatacell.newInstance(leURIContent);
	}
	
	
	/**
	 * cette methode a pour but de traduire un URIPortObject vers un URI.
	 * Nayant pas acces au type statique URIPortObject (ce qui serai plus facile),
	 * je dois le r�cuperer en tant que type dynamique (rappel : type statique PortObject). 
	 * je dois faire � la main ce qui aurait pris les instructions suivantes: 
	 * public execute (URIPortObject inData)...
	 * 
	 * URI leURI = inData.getURIContent().getURI();
	 * 
	 * (pour les NOOBS en POO, un type statique est le type qua une variable � la compilation.
	 * un type dynamique c'est le type de l'object quelle contient
	 * 
	 *  exemple : 
	 *  URI unURI = new URI(); //type statique URI et dynamique URI;
	 *  Object unObject = unURI; //type statique Object et type dynamique URI
	 *  )
	 *  
	 *  @param lePort le port object quon veut traduire
	 *  @return le URI 
	 */
	public static URI portToURI(PortObject lePort) throws Exception {
		//r�cupere le type dynamique du port (normalement un URIPortObject) (il le sauvegarde pour le retour de execute)
		URIPortObject = lePort.getClass();
		
		//r�cupere la m�thode getURIContent
		Method getURIContents = URIPortObject.getMethod("getURIContents");
		
		//cast le PortObject vers un URIPortObject
		Object leURIPortObject = URIPortObject.cast(lePort);
	
		//invoque la methode getURIContents et récupere le URIContent contenant l'URI
		Object leURIContent = ((Collection<?>) getURIContents.invoke(leURIPortObject, new Object[] {})).iterator().next();
		
		//r�cupere la class URIContent affin de récuperer la méthode get URI + SAUVEGARDE
		URIContent = leURIContent.getClass();
		
		//r�cupere la methode getURI
		Method getURI = URIContent.getMethod("getURI", new Class<?>[]{});
		
		//r�cupere le URI en invoquant la méthode getURI sur un URIContent
		URI leURI = (URI) getURI.invoke(leURIContent, new Object[] {});
		
		return leURI;
	}
	
	/**
	 * comme portToURI mais � l'envers,
	 * comme on cr�e des instances des classes on doit r�cuperer les constructeurs
	 */
	public static PortObject uriToPort(URI leURI) throws Exception {
		//récupere le constructeur de la classe URIContent
		Constructor<?> Curicontent = URIContent.getConstructor(new Class<?>[] {URI.class, String.class});
		//crée une instance de URIContent, contenant le URI.
		Object leURIContent = Curicontent.newInstance(leURI, "");
		//récupere le constructeur de la classe URIPortObject
		Constructor<?> CuriPortObject = URIPortObject.getConstructor(new Class<?>[] {List.class});
		//crée une list qui va contenir notre URIContent (car le constructeur n'accepte que une liste.
		List<Object> listURIContent = new ArrayList<Object>();
		listURIContent.add(leURIContent);
		
		//crée une instance de URIPortObject, contenant notre URIContent, luimeme contenant notre URI.
		PortObject leURIPortObject = (PortObject) CuriPortObject.newInstance(listURIContent);
		
		return leURIPortObject;
	}
}