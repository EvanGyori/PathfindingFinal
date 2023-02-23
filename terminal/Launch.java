/**
 * Lauch the default system application.
 * 
 * @see <a href="https://stackoverflow.com/questions/5226212/how-to-open-the-default-webbrowser-using-java">launch</a>
 */
import java.awt.Desktop;
import java.awt.Desktop.*;
import java.io.*;
import java.net.*;
public class Launch{
	
	public static void main(String[]args){
		launch("https://www.google.com");
	}
	/**
	 * Launches the application associated with the given path including the browser, media player, and photo viewer.
	 * 
	 * Supports Windows, Mac, and Linux.
	 * @param url the page/file to open
	 * @throws IllegalArgumentException if the url is null
	 */
	public static void launch(String url){
		if(url == null){
			throw new IllegalArgumentException("url cannot be null");
		}
		if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)){
			Desktop desktop = Desktop.getDesktop();
			try{
				desktop.browse(new URI(url));
			}catch(IOException ioe){
				ioe.printStackTrace();
			}catch(URISyntaxException urise) {
				urise.printStackTrace();
			}
		}else{ 
			Runtime runtime = Runtime.getRuntime();
			try{
				runtime.exec("xdg-open " + url);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
