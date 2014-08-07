package tags;

import groovy.lang.Closure;
import play.templates.*;
import play.templates.GroovyTemplate.ExecutableTemplate;
import java.io.PrintWriter;
import java.util.Map;

import play.mvc.Http;
import play.mvc.Router.ActionDefinition;
import play.mvc.Scope.Session;

@FastTags.Namespace("CustomTag")
public class CustomTag extends FastTags {

	/**
	 * Generates a html form element linked to a controller action
	 * 
	 * @param args
	 *            tag attributes
	 * @param body
	 *            tag inner body
	 * @param out
	 *            the output writer
	 * @param template
	 *            enclosing template
	 * @param fromLine
	 *            template line number where the tag is defined
	 */
	public static void _form(Map<?, ?> args, Closure body, PrintWriter out,
			ExecutableTemplate template, int fromLine) {
		ActionDefinition actionDef = (ActionDefinition) args.get("arg");
		if (actionDef == null) {
			actionDef = (ActionDefinition) args.get("action");
		}
		String enctype = (String) args.get("enctype");
		if (enctype == null) {
			enctype = "application/x-www-form-urlencoded";
		}
		if (actionDef.star) {
			actionDef.method = "POST"; // prefer POST for form ....
		}
		if (args.containsKey("method")) {
			actionDef.method = args.get("method").toString();
		}
		String name = null;
		if (args.containsKey("name")) {
			name = args.get("name").toString();
		}
		String id = args.containsKey("id") ? " id=\"" + args.get("id") + "\""
				: "";
		String clz = args.containsKey("class") ? " class=\""
				+ args.get("class") + "\"" : "";

		if (!("GET".equals(actionDef.method) || "POST".equals(actionDef.method))) {
			String separator = actionDef.url.indexOf('?') != -1 ? "&" : "?";
			actionDef.url += separator + "x-http-method-override="
					+ actionDef.method.toUpperCase();
			actionDef.method = "POST";
		}
		String encoding = Http.Response.current().encoding;
		out.print("<form action=\""
				+ actionDef.url
				+ "\" method=\""
				+ actionDef.method.toLowerCase()
				+ "\" accept-charset=\""
				+ encoding
				+ "\" enctype=\""
				+ enctype
				+ "\" "
				+ serialize(args, "action", "method", "accept-charset",
						"enctype")
				+ (name != null ? "name=\"" + name + "\"" : "") + id + clz
				+ ">");
		if (!("GET".equals(actionDef.method))) {
			_authenticityToken(args, body, out, template, fromLine);
		}
		out.println(JavaExtensions.toString(body));
		out.print("</form>");
	}

	public static void _authenticityToken(Map<?, ?> args, Closure body,
			PrintWriter out, ExecutableTemplate template, int fromLine) {
		out.println("<input type=\"hidden\" name=\"authenticityToken\" value=\""
				+ Session.current().getAuthenticityToken() + "\">\\");
	}
}