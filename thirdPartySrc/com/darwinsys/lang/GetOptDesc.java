/*
 * Copyright (c) Ian F. Darwin, ian@darwinsys.com, 1996-2004.
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: GetOptDesc.java,v 1.1 2005/02/17 18:56:45 paulreiners Exp $
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

/** A GetOptDesc describes one argument that may be accepted by the program.
 */
public class GetOptDesc {
	/** The short-form option letter */
	protected char argLetter;
	/** The long-form option name */
	protected String argName;
	/** True if this option needs an argument after it */
	protected boolean takesArgument;

	/** Construct a GetOpt option.
	 * @param ch The single-character name for this option.
	 * @param nm The word name for this option.
	 * @param ta True if this option requires an argument after it.
	 */
	public GetOptDesc(char ch, String nm, boolean ta) {
		if (!Character.isLetter(ch) && !Character.isDigit(ch)) {
			throw new IllegalArgumentException(ch + ": not letter or digit");
		}
		argLetter = ch;
		argName   = nm;	// may be null, meaning no long name.
		takesArgument = ta;
	}
}
