

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

/**
 * Servlet implementation class SignUpAction
 */
@WebServlet(description = "Servlet For Sign up", urlPatterns = { "/SignUpAction" })
public class SignUpAction extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(SignUpAction.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpAction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String signup_usrname = request.getParameter("signup_usrname").toString();
		String signup_email = request.getParameter("signup_email").toString();
		String signup_password = request.getParameter("signup_password").toString();
		log.debug("Sign Up Servlet: [" + signup_usrname + "] [" + signup_email + "] [" + signup_password + "] want to sign up");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");	
		
		int rtv = 0;
		if(UserManager.existUsrName(signup_usrname)){
			log.info("User name [" + signup_usrname + "] has been in DB");
			rtv = 1;
		} else {
			log.info("Insert user name [" + signup_usrname + "]");
			UserManager.insertUser(signup_usrname, signup_email, signup_password);
			UserManager.incTotalUserNumByOne();
			UserManager.publishTotalUserNum();	
		}

		String json = new Gson().toJson(rtv);
		System.out.println(json);
		response.getWriter().write(json);	
	}

}
