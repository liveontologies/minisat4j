package com.github.liveontologies.ipasir4j.minisat;

/*-
 * #%L
 * Java Bindings for the Minisat solver
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2020 Live Ontologies Project
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.liveontologies.ipasir4j.IpasirSolver;
import com.github.liveontologies.ipasir4j.jna.IpasirNativeSolver;
import com.github.liveontologies.ipasir4j.jna.JNAIpasir;
import com.sun.jna.Native;

public class Minisat {

	private final static JNAIpasir MINISAT_JNA = Native.load("minisat",
			JNAIpasir.class);

	/**
	 * @return the name and the version of the incremental SAT solving library
	 */
	public static String getSignature() {
		return MINISAT_JNA.ipasir_signature();
	}

	/**
	 * @return a new solver instance
	 */
	public static IpasirSolver createSolver() {
		return new IpasirNativeSolver(MINISAT_JNA);
	}

}
