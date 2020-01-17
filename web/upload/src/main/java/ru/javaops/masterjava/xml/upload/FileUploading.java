package ru.javaops.masterjava.xml.upload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;


@WebServlet(name = "upload", urlPatterns = "/upload")
public class FileUploading extends HttpServlet {

  private StaxStreamProcessor processor;



  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    List<User> users = Collections.emptyList();
    if(ServletFileUpload.isMultipartContent(request)){
      DiskFileItemFactory factory = new DiskFileItemFactory();


      ServletContext servletContext = this.getServletConfig().getServletContext();
      File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
      factory.setRepository(repository);
      ServletFileUpload upload = new ServletFileUpload(factory);
      InputStream uploadedStream = null;
      try {
        List<FileItem> items = upload.parseRequest(request);
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
          FileItem item = iter.next();

          if (!item.isFormField()) {
            uploadedStream = item.getInputStream();
            processor = new StaxStreamProcessor(uploadedStream);
            JaxbParser parser = new JaxbParser(User.class);
            while (processor.startElement("User","Users")){
              User user = parser.unmarshal(processor.getReader(), User.class);
              users.add(user);
            }


          }
        }
      } catch (FileUploadException | XMLStreamException e) {
        e.printStackTrace();
      } catch (JAXBException e) {
        e.printStackTrace();
      }
      finally {
        uploadedStream.close();
      }
      request.setAttribute("users",users);
      request.getRequestDispatcher("/users.jsp").forward(request,response);
    }


  }


}
