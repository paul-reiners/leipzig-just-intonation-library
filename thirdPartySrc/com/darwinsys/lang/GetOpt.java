/*
 * Copyright (c) Ian F. Darwin, ian@darwinsys.com, 1996-2004.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: GetOpt.java,v 1.1 2005/02/17 18:56:45 paulreiners Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the author nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

package com.darwinsys.lang;

import com.darwinsys.util.Debug;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/** A class to implement UNIX-style (single-character) command line argument
 * parsing. Originally patterned after (but not using code from) the UNIX 
 * getopt(3) program, this has been redesigned to be more Java-friendly.
 * As a result, there are two ways of using it.
 * <ol><li>Original model:
 * <pre>
        GetOpt go = new GetOpt("hno:");
        boolean numeric_option = false;
        String outFileName = "(standard output)";
        char c;
        while ((c = go.getopt(args)) != GetOpt.DONE) {
            switch(c) {
            case 'h':
                doHelp(0);
                break;
            case 'n':
                numeric_option = true;
                break;
            case 'o':
                outFileName = go.optarg();
                break;
            default:
                System.err.println("Unknown option character " + c);
                doHelp(1);
            }
        }
        System.out.print("Options: ");
        System.out.print("Numeric: " + numeric_option + ' ');
        System.out.print("Output: " + outFileName + "; ");
        System.out.print("Inputs: ");
        if (go.getOptInd() == args.length) {
            doFile("(standard input)");
        } else for (int i = go.getOptInd(); i < args.length; i++) {
            doFile(args[i]);
        }
 * </pre></li>
 * <li>Newer model, which allows long-named options:
 * <pre>
        boolean numeric_option = false;
        boolean errs = false;
        String outputFileName = null;

        GetOptDesc options[] = {
            new GetOptDesc('n', "numeric", false),
            new GetOptDesc('o', "output-file", true),
        };
        GetOpt parser = new GetOpt(options);
        Map optionsFound = parser.parseArguments(argv);
        Iterator it = optionsFound.keySet().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();
            char c = key.charAt(0);
            switch (c) {
                case 'n':
                    numeric_option = true;
                    break;
                case 'o':
                    outputFileName = (String)optionsFound.get(key);
                    break;
                case '?':
                    errs = true;
                    break;
                default:
                    throw new IllegalStateException(
                    "Unexpected option character: " + c);
            }
        }
        if (errs) {
            System.err.println("Usage: GetOptDemo [-n][-o file][file...]");
        }
        System.out.print("Options: ");
        System.out.print("Numeric: " + numeric_option + ' ');
        System.out.print("Output: " + outputFileName + "; ");
        System.out.print("Input files: ");
        List files = parser.getFilenameList();
        while (files.hasNext()) {
            System.out.print(files.next());
            System.out.print(' ');
        }
        System.out.println();
	}
 * </pre></li>
 * </ol>
 * <p>
 * This is <em>not</em> threadsafe; it is expected to be used only from main().
 * <p>
 * For another way of dealing with command lines, see the
 * <a href="http://jakarta.apache.org/commons/cli/">Jakarta Commons
 * Command Line Interface</a>.
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: GetOpt.java,v 1.1 2005/02/17 18:56:45 paulreiners Exp $
 */
public class GetOpt {
	/** The List of File Names found after args */
	protected List fileNameArguments;
	/** The set of characters to look for */
	protected final GetOptDesc[] options;
	/** Where we are in the options */
	protected int optind = 0;
	/** Public constant for "no more options" */
	public static final int DONE = 0;
	/** Internal flag - whether we are done all the options */
	protected boolean done = false;
	/** The current option argument. */
	protected String optarg;

	/** Retrieve the current option argument; UNIX variant spelling. */
	public String optarg() {
		return optarg;
	}
	/** Retrieve the current option argument; Java variant spelling. */
	public String optArg() {
		return optarg;
	}

	/* Construct a GetOpt parser, given the option specifications
	 * in an array of GetOptDesc objects. This is the preferred constructor.
	 */
	public GetOpt(final GetOptDesc[] opt) {
		this.options = opt;
	}

	/* Construct a GetOpt parser, storing the set of option characters.
	 * This is a legacy constructor for backwards compatibility.
	 */
	public GetOpt(final String patt) {
		if (patt == null) {
			throw new IllegalArgumentException("Pattern may not be null");
		}

		// Pass One: just count the letters
		int n = 0;
		for (int i = 0; i<patt.length(); i++) {
			if (patt.charAt(i) != ':')
				++n;
		}
		if (n == 0) {
			throw new IllegalArgumentException(
				"No option letters found in " + patt);
		}

		// Pass Two: construct an array of GetOptDesc opjects.
		options = new GetOptDesc[n];
		for (int i = 0, ix = 0; i<patt.length(); i++) {
			final char c = patt.charAt(i);
			boolean argTakesValue = false;
			if (i < patt.length() - 1 && patt.charAt(i+1) == ':') {
				argTakesValue = true;
				++i;
			}
			options[ix] = new GetOptDesc(c, null, argTakesValue);
			Debug.println("getopt",
				"CONSTR: options[" + ix + "] = " + c + ", " + argTakesValue);
			++ix;
		}
	}

	/** Reset this GetOpt parser */
	public void rewind() {
		fileNameArguments = null;
		done = false;
		optind = 0;
	}

	/** Array used to convert a char to a String */
	private static char[] strConvArray = { 0 };

	/** 
	 * Modern way of using GetOpt: call this once and get all options.
	 * <p>
	 * This parses the options, returns a Map whose keys are the found options.
	 * Normally followed by a call to getFilenameList().
	 * @return a Map whose keys are Strings of length 1 (containing the char
	 * from the option that was matched) and whose value is a String
	 * containing the value, or null for a non-option argument.
	 */
	public Map parseArguments(String[] argv) {
		Map optionsAndValues = new HashMap();
		fileNameArguments = new ArrayList();
		for (int i = 0; i < argv.length; i++) {
			Debug.println("getopt", "parseArg: i=" + i + ": arg " + argv[i]);
			char c = getopt(argv);
			if (c != DONE) {
				strConvArray[0] = c;
				optionsAndValues.put(new String(strConvArray), optarg);
				// If this arg takes an option, we must skip it here.
				if (optarg != null)
					++i;
			} else {
				fileNameArguments.add(argv[i]);
			}
		}
		return optionsAndValues;
	}

	/** Get the list of filename-like arguments after options */
	public List getFilenameList() {
		if (fileNameArguments == null) {
			throw new IllegalArgumentException(
				"Illegal call to getFilenameList() before parseOptions()");
		}
		return fileNameArguments;
	}

	/** The true heart of getopt, whether used old way or new way:
	 * returns one argument; call repeatedly until it returns DONE.
	 */
	public char getopt(String argv[]) {
		Debug.println("getopt",
			"optind=" + optind + ", argv.length="+argv.length);

		if (optind >= (argv.length)-1) {
			done = true;
		}

		// If we are (now) finished, bail.
		if (done) {
			return DONE;
		}

		// XXX TODO - two-pass, 1st check long args, 2nd check for
		// char, may be multi char as in "-no outfile" == "-n -o outfile".

		// Pick off the next command line argument, check if it starts "-".
		// If so look it up in the list.
		String thisArg = argv[optind++];
		if (thisArg.startsWith("-")) {
			optarg = null;
			for (int i=0; i<options.length; i++) {
				if ( options[i].argLetter == thisArg.charAt(1) ||
					(options[i].argName != null &&
					 options[i].argName == thisArg.substring(1))) { // found it
					// If it needs an option argument, get it.
					if (options[i].takesArgument) {
						if (optind < argv.length) {
							optarg = argv[optind]; 
							++optind;
						} else {
							throw new IllegalArgumentException(
								"Option " + options[i].argLetter +
								" needs value but found end of arg list");
						}
					}
					return options[i].argLetter;
				}
			}
			// Began with "-" but not matched, so must be error.
			return '?';
		} else {
			// Found non-argument non-option word in argv: end of options.
			done = true;
			return DONE;
		}
	}

	/** Return optind, the index into args of the last option we looked at */
	public int getOptInd() {
		return optind;
	}

}
