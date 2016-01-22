import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

public class StubGenerator {

	public static void main(String[] argv) {

		if (argv.length != 1) {
			System.out.println("java StubGenerator <nom de la classe>");
			return;
		}

		String className = argv[0];
		FileWriter fw = null;

		try {
			Class<?> classStub = Class.forName(className + "_itf");

			File fileStub = new File("src/" + className + "_stub.java");
			System.out.println(fileStub.getAbsolutePath());
			fileStub.createNewFile();

			fw = new FileWriter(fileStub);

			fw.write("/** @Generated */ \n"
					+ "public class " + className + "_stub extends SharedObject implements " + classStub.getName() + ", java.io.Serializable {\n\n");

			fw.write("\t private static final long serialVersionUID = 1L;\n\n");
			/*
			for (Field field : classStub.getFields()) {
				fw.write(Modifier.toString(field.getModifiers()) + " " + field.getType().getName() + " " + field.getName() + "\n");
			}
			fw.write("\n");
			 */

			fw.write("\t public " + className + "_stub(Object o, int id){\n"
					+ "\t \t super(o, id);\n"
					+ "\t }\n\n");

			for (Method meth : classStub.getMethods()) {

				fw.write("\t ");
				// On écrit les modifiers sauf abstract
				for (String s : Modifier.toString(meth.getModifiers()).split(" abstract")) {
					fw.write(s);
				}

				// On écrit ensuite le type du retour de la methode et son nom
				fw.write(" " + meth.getReturnType().getName() + " " + meth.getName() + "(");
				// On écrit chaque paramètres (avec une virgule avant, sauf pour le premier)
				for (int i=0; i<meth.getParameters().length; i++) {
					if (i!=0) {
						fw.write(", ");
					}
					Parameter param = meth.getParameters()[i];
					fw.write(param.getType().getName() + " " + param.getName());
				}
				fw.write(")");
				// On écrit les exceptions
				if (meth.getExceptionTypes().length!=0) {
					fw.write(" throws ");
					for (Class<?> eClass : meth.getExceptionTypes()) {
						fw.write(eClass.getName() + " ");
					}
				}
				fw.write("{\n");
				// On teste si la méthode est @Read et/ou @Write
				if (meth.getAnnotation(Write.class)!=null) {
					fw.write("\t \t this.lock_write();\n");
				}
				if (meth.getAnnotation(Read.class)!=null) {
					fw.write("\t \t this.lock_read();\n"
							+ "\t \t " + meth.getReturnType().getName() + " returnReadFor" + className + "Stub  = ");
				}
				if ((meth.getAnnotation(Read.class)!=null) || (meth.getAnnotation(Write.class)!=null)) {
					if (meth.getAnnotation(Read.class)==null) {
						fw.write("\t \t ");
					}
					fw.write("((" + className + ") this.getO())." + meth.getName() + "(");
					for (int i=0; i<meth.getParameters().length; i++) {
						if (i!=0) {
							fw.write(", ");
						}
						Parameter param = meth.getParameters()[i];
						fw.write(param.getName());
					}
					fw.write(");\n"
							+ "\t \t if (Transaction.getCurrentTransaction()==null) {\n"
							+ "\t \t \t this.unlock();\n"
							+ "\t \t }\n");
					if (meth.getAnnotation(Read.class)!=null) {
						fw.write("\t \t return returnReadFor" + className + "Stub;\n");
					}
				} else {
					if (meth.getReturnType()!=void.class) {
						if (meth.getReturnType().isPrimitive()) {
							switch (meth.getReturnType().getName()) {
							
							case "boolean" :
								fw.write("\t \t return true;\n");
								break;
							case "char" :
								fw.write("\t \t return 'a';\n");
								break;
							case "byte" :
								fw.write("\t \t return 0;\n");
								break;
							case "short" :
								fw.write("\t \t return 0;\n");
								break;
							case "int" :
								fw.write("\t \t return 0;\n");
								break;
							case "long" :
								fw.write("\t \t return 0;\n");
								break;
							case "float" :
								fw.write("\t \t return 0.0F;\n");
								break;
							case "double" :
								fw.write("\t \t return 0.0;\n");
								break;
							}
						} else {
							fw.write("\t \t return (new " + meth.getReturnType().getName() + "());\n");
						}
					}
				}
				fw.write("\t }\n\n");
			}
			fw.write("\n");

			fw.write("}");
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

}
