package com.haks.haksvn.source.util;

import java.util.LinkedList;

import com.haks.haksvn.source.util.DiffMatchPatchUtils.Diff;

public class DiffMatchTest {

	
	
	public static void main(String[] args) throws Exception{
		
		String text1 = "Hamleta: Do you see yonder cloud that's almost in shape of a camel?\n"
					+ "Polonius: By the mass, and 'tis like a camel, indeed.\n"
					+ "Hamlet: Methinks it is like a weasel.\n"
					+ "Polonius: It is backed like a weasel.\n"
					+ "Hamlet: Or like a whale?\n"
					+ "Polonius: Very like a whale3.\n"
					+ "-- Shakespeare";	
	
		String text2 = "Hamlets: Do you see yonder cloud that's almost in shape of a camel?\n"
					+ "Hamlet: Methinks it is like a weasel.\n"
					+ "Polonius: It is backed like a weasel.\n"
					+ "Hamlet: Or like a whale?\n"
					+ "Polonius: Very like a whales1.\n"
					+ "-- Shakespeare";
		
		LinkedList<Diff> diffList = new DiffMatchPatchUtils().diff_main(text1, text2, true);
		
		for( Diff diff : diffList ){
			System.out.println( diff.operation + ", "  + diff.text);
		}
	}
}
