package edu.stevens.cs548.owl;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.logging.Logger;

/*
 * CLI for a DHT node.
 */

public class CliClient implements Inferencer.Console {

	public static Logger log = Logger.getLogger(CliClient.class.getCanonicalName());

	protected Inferencer inferencer = new Inferencer();

	public CliClient() {
	}

	protected void msg(String m) {
		System.out.print(m);
		;
	}

	public void msgln(String m) {
		System.out.println(m);
		System.out.flush();
	}
	
	public OutputStream getOutputStream() {
		return System.out;
	}

	protected void err(Exception e) {
		System.err.println("Error : " + e);
		e.printStackTrace();
	}

	public void cli() {

		// Main command-line interface loop

		Dispatch d = new Dispatch();
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			while (true) {
				msg("owl> ");
				String line = in.readLine();
				String[] inputs = line.split("\\s+");
				if (inputs.length > 0) {
					String cmd = inputs[0];
					if (cmd.length() == 0)
						;
					else if ("debug".equals(cmd))
						d.debug(inputs);
					else if ("namespace".equals(cmd))
						d.namespace(inputs);
					else if ("schema".equals(cmd))
						d.schema(inputs);
					else if ("all".equals(cmd))
						d.all(inputs);
					else if ("props".equals(cmd))
						d.props(inputs);
					else if ("elems".equals(cmd))
						d.elems(inputs);
					else if ("isa".equals(cmd))
						d.isa(inputs);
					else if ("help".equals(cmd))
						d.help(inputs);
					else if ("quit".equals(cmd))
						return;
					else
						msgln("Bad input.  Type \"help\" for more information.");
				}
			}
		} catch (EOFException e) {
		} catch (IOException e) {
			err(e);
			System.exit(-1);
		}

	}

	protected class Dispatch {

		protected

		Dispatch() {

		}

		public void help(String[] inputs) {
			if (inputs.length == 1) {
				msgln("Commands are:");
				msgln("  namespace uri: set the namespace uri for resource names");
				msgln("  schema filename: load OWL definitions");
				msgln("  all: list all triples in the knowledge base");
				msgln("  props res: list the properties for a resource");
				msgln("  elems class: list instances in the class");
				msgln("  isa res class: is the resource in the class?");
				msgln("  debug: toggle debugging output");
				msgln("  quit: exit");
			}
		}

		public void namespace(String[] inputs) {
			if (inputs.length == 2) {
				inferencer.namespace(inputs[1]);
			} else {
				msgln("Usage: namespace <uri>");
			}
		}

		public void schema(String[] inputs) {
			if (inputs.length == 2) {
				inferencer.schema(CliClient.this, inputs[1]);
			} else {
				msgln("Usage: schema <filename>");
			}
		}

		public void props(String[] inputs) {
			if (inputs.length == 2) {
				inferencer.props(CliClient.this, inputs[1]);
			} else {
				msgln("Usage: props <res-name>");
			}
		}

		public void elems(String[] inputs) {
			if (inputs.length == 2) {
				inferencer.elems(CliClient.this, inputs[1]);
			} else {
				msgln("Usage: elems <class-name>");
			}
		}

		public void isa(String[] inputs) {
			if (inputs.length == 3) {
				if (inferencer.isa(inputs[1], inputs[2])) {
					msgln(inputs[1] + " IS recognized as a " + inputs[2]);
				} else {
					msgln(inputs[1] + " is NOT recognized as a " + inputs[2]);
				}
			} else {
				msgln("Usage: isa <res-name> <class-name>");
			}
		}
		
		public void all(String[] inputs) {
			if (inputs.length == 1) {
				inferencer.allTriples(CliClient.this);
			} else {
				msgln("Usage: all");
			}
		}
		
		public void debug(String[] inputs) {
			if (inputs.length == 1) {
				inferencer.toggleDebug();
			} else {
				msgln("Usage: debug");
			}
		}

	}

}
