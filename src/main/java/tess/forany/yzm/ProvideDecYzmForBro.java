package tess.forany.yzm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import tess.fornay.domain.CopyOfCleanLines;

/**
 * Servlet implementation class ProvideDecYzmForBro
 */
public class ProvideDecYzmForBro extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProvideDecYzmForBro() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ITesseract ins = new Tesseract();  // JNA Interface Mapping  
		ins.setDatapath(request.getRealPath("/tessdata")); 
		//System.load("/usr/local/lib/libtesseract.so");
		//System.load("/usr/local/lib/liblept.so");
        UUID u=UUID.randomUUID();		
        String filename=u.toString()+"."+request.getParameter("fileSuffixName");
		InputStream input =request.getInputStream();
		File file=new File(request.getRealPath("image/"+filename));
		if(!file.exists()){
			file.createNewFile();
		}
		byte b[]=new byte[1024];
		OutputStream out=new FileOutputStream(file.getAbsolutePath());
		while(input.read(b)!=-1){
			out.write(b);
		}
		out.flush();
		if (input!=null) {
			input.close();
		}
		if (out!=null) {
			out.close();
		}
		 String valCode="";
		try {

			CopyOfCleanLines.cleanLinesInImage(file,request.getRealPath("image/"));
			valCode = ins.doOCR(file);
			if(file.exists()){
				file.delete();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write(e.toString());
		}
		 valCode=valCode.replace(" ","").replace("бу", "0").replace("ву","6");
		 response.getWriter().write(valCode);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
