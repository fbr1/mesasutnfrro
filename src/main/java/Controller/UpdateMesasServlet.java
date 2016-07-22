package Controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@SuppressWarnings("serial")
public class UpdateMesasServlet extends HttpServlet {

    private static final long UPDATE_INTERVAL = 2160000; // An hour in milliseconds
    final Logger logger = LoggerFactory.getLogger(UpdateMesasServlet.class);


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("text/html");//setting the content type
        PrintWriter pw = resp.getWriter();//get the stream to write the data
        pw.println("<html><body>");

        if(isTimeForUpdate()){
            pw.println("Es tiempo de una actualizacion");
        }

        pw.println("</body></html>");

        pw.close();//closing the stream

    }
    /**
     * Returns true if enough time has passed for a new update to be allowed.
     * The time required is set on the constant UPDATE_INTERVAL
     *
     * @return      whether is time or not for an update
     */
    private boolean isTimeForUpdate(){

        ServletContext application = getServletContext();
        Long currentTime = System.currentTimeMillis();
        String lastUpdate = (String) application.getAttribute("lastupdate");

        if (lastUpdate == null || lastUpdate.isEmpty()) {

            // If it's the first execution of the webapp instance
            application.setAttribute("lastupdate", Long.toString(currentTime));
            return true;

        } else {

            long timeSinceLastUpdate = currentTime - Long.valueOf(lastUpdate);

            if (timeSinceLastUpdate > UPDATE_INTERVAL) {
                // Update last update time
                lastUpdate = Long.toString(currentTime);
                application.setAttribute("lastupdate", lastUpdate);
                return true;
            }
        }
        return false;
    }

}

