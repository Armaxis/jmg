/*

<This Java Class is part of the jMusic API version 1.5, March 2004.>
:30  2001

Copyright (C) 2000 Andrew Sorensen & Andrew Brown

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or any
later version.

This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.

*/ 

package jm.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import jm.JMC;
import jm.midi.SMF;
import jm.music.data.CPhrase;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;

public class Write implements JMC{
    
   
    //----------------------------------------------
    // MIDI
    //----------------------------------------------
    /**
    * Save the jMusic score as a standard MIDI file
    * Prompt user for a filename
    * @param Score
    */ 
    public static void midi(Score score) {
        FileDialog fd = new FileDialog(new Frame(), 
                                       "Save as a MIDI file ...", 
                                       FileDialog.SAVE);
        fd.setFile("jMusic_composition.mid");
        fd.show();
        if (fd.getFile() != null) {
            Write.midi(score, fd.getDirectory() + fd.getFile());                
        }
        
    }
    
    /**
     * Write the score to outputStream as a standard MIDI file
     * @param Score
     * @param OutputStream outputStream
     */ 
    public static void midi(Score scr, OutputStream outputStream) {        
        //Score s = adjustTempo(scr);
        SMF smf = new SMF();
	try{
		smf.clearTracks();
		jm.midi.MidiParser.scoreToSMF(scr,smf);
            smf.write(outputStream);
        } catch(IOException e) {
			System.err.println(e);
		}
    }
    
    /**
    * Save the jMusic score as a standard MIDI file
    * @param Score
    * @param String filename
    */ 
    public static void midi(Score scr, String fileName) {        
        //Score s = adjustTempo(scr);
        SMF smf = new SMF();
		try{
                    double time1 = System.currentTimeMillis();		
                    System.out.println("----------------------------- Writing MIDI File ------------------------------");
			smf.clearTracks();
			jm.midi.MidiParser.scoreToSMF(scr,smf);
			OutputStream os = new FileOutputStream(fileName);
			smf.write(os);
			double time2 = System.currentTimeMillis();		
			System.out.println("MIDI file '"+fileName+"' written from score '" +
                            scr.getTitle()+"' in "+ ((time2-time1)/1000)+ " seconds.");
			System.out.println("------------------------------------------------------------------------------");
		}catch(IOException e){
			System.err.println(e);
		}
    }
    
    /**
    * Save the jMusic part as a standard MIDI file
    * Use the part title as a the filename
    * @param Part
    */ 
    public static void midi(Part p) {
        midi(new Score(p));
    }
    
    /**
    * Save the jMusic part as a standard MIDI file
    * @param Part
    * @param String filename
    */ 
    public static void midi(Part p, String fileName) {  
        Score s = new Score( "Score of "+ p.getTitle());
        s.addPart(p);
        midi(s, fileName);
    }
    
    /**
    * Save the jMusic phrase as a standard MIDI file
    * Use the phrase title as a the filename
    * @param Phrase
    */ 
    public static void midi(Phrase phr) {
        midi(new Score(new Part(phr)));
    }
    
    /**
    * Save the jMusic phrase as a standard MIDI file
    * @param Phrase
    * @param String filename
    */ 
    public static void midi(Phrase phr, String fileName) {  
        Part p = new Part();
        p.addPhrase(phr);
        Score s = new Score( "Score of "+ phr.getTitle());
        s.addPart(p);
        midi(s, fileName);
    }
    
    /**
    * Save the jMusic CPhrase as a standard MIDI file
    * Use the phrase title as a the filename
    * @param CPhrase
    */ 
    public static void midi(CPhrase cphr) {
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        midi(s, cphr.getTitle()+".mid");
    }
    
    /**
    * Save the jMusic cphrase as a standard MIDI file
    * @param CPhrase
    * @param String filename
    */ 
    public static void midi(CPhrase cphr, String fileName) {  
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        midi(s, fileName);
    }

	/**
	* Save the jMusic note as a standard MIDI file
	 * Use the note title as a the filename
	 * @param Note The note to be saved.
	 */
	public static void midi(Note n) {
		midi(n, "SingleNote.mid");
	}

	/**
	* Save the jMusic note as a standard MIDI file
	 * @param Note The note to be saved.
	 * @param String filename The name of the written file.
	 */
	public static void midi(Note n, String fileName) {
		Score s = new Score( "Score of a single note");
		Part p = new Part(new Phrase(n));
		s.addPart(p);
		midi(s, fileName);
	}
	
    
    //----------------------------------------------
    // jm
    //----------------------------------------------
    /**
    * Save the jMusic score as a jMusic file
    * Use the score title as a the filename
    * @param Score
    */ 
    public static void jm(Score s) {
        jm(s, s.getTitle()+".jm");
    }
    
    /**
    * Save the jMusic score as a jMusic file
    * @param Score
    * @param String filename
    */ 
    public static void jm(Score s, String fileName) {        
		try{
		    System.out.println("--------------------- Writing JM File -----------------------");
			FileOutputStream fos = new FileOutputStream(fileName);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(s);
			oos.flush();
			oos.close();
			System.out.println("JM file '"+fileName+"' written from score '" +s.getTitle()+"'");
			System.out.println("-------------------------------------------------------------");
		}catch(IOException e){
			System.err.println(e);
		}
    }
    
    /**
    * Save the jMusic Part as a jMusic file
    * Use the Part title as a the filename
    * @param Part
    */ 
    public static void jm(Part p) {
        Score s = new Score("Score of "+ p.getTitle());
        s.addPart(p);
        jm(s, p.getTitle()+".jm");
    }
    
    /**
    * Save the jMusic Part as a jMusic file
    * @param Part
    * @param String filename
    */ 
    public static void jm(Part p, String fileName) {
        Score s = new Score("Score of "+ p.getTitle());
        s.addPart(p);
        jm(s, fileName);
    }
    
    /**
    * Save the jMusic Phrase as a jMusic file
    * Use the phrase title as a the filename
    * @param Phrase
    */ 
    public static void jm(Phrase phr) {
        Part p = new Part();
        p.addPhrase(phr);
        Score s = new Score("Score of "+ phr.getTitle());
        s.addPart(p);
        jm(s, phr.getTitle()+".jm");
    }
    
    /**
    * Save the jMusic Phrase as a jMusic file
    * @param Phrase
    * @param String filename
    */ 
    public static void jm(Phrase phr, String fileName) {
        Part p = new Part();
        p.addPhrase(phr);
        Score s = new Score("Score of "+ phr.getTitle());
        s.addPart(p);
        jm(s, fileName);
    }
    
    /**
    * Save the jMusic CPhrase as a jMusic file
    * Use the cphrase title as a the filename
    * @param CPhrase
    */ 
    public static void jm(CPhrase cphr) {
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        jm(s, cphr.getTitle()+".jm");
    }
    
    /**
    * Save the jMusic CPhrase as a jMusic file
    * @param CPhrase
    * @param String filename
    */ 
    public static void jm(CPhrase cphr, String fileName) {
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        jm(s, fileName);
    }

    //----------------------------------------------
    // XML
    //----------------------------------------------
	
	/** 
	 * Save the jMusic Score to an xml file
	 * @param Score
	 */
    public static void xml(Score s) {
        xml(s, s.getTitle()+".xml");
    }

	/** 
	 * Save the jMusic Score to an xml file
	 * @param Score
	 * @param String - the file name to write to 
	 */
    public static void xml(Score s, String fileName){
		try{
			PrintWriter pw = new PrintWriter(new FileWriter(fileName));
            System.out.println("--------------------- Writing XML File -----------------------");
            String xmlString = XMLParser.scoreToXMLString(s);
            //System.out.println(xmlString);
            pw.print(xmlString);
            pw.close();
            System.out.println("XML file '"+fileName+"' written from score '" +s.getTitle()+"'");
			System.out.println("-------------------------------------------------------------");
		}catch(IOException e){
			System.err.println(e);
		}
        
	}
	
	/**
    * Save the jMusic Part as an xml file
    * Use the Part title as a the filename
    * @param Part
    */ 
    public static void xml(Part p) {
        Score s = new Score("Score of "+ p.getTitle());
        s.addPart(p);
        xml(s, p.getTitle()+".xml");
    }
    
    /**
    * Save the jMusic Part as a xml file
    * @param Part
    * @param String filename
    */ 
    public static void xml(Part p, String fileName) {
        Score s = new Score("Score of "+ p.getTitle());
        s.addPart(p);
        xml(s, fileName);
    }
    
    /**
    * Save the jMusic Phrase as a xml file
    * Use the phrase title as a the filename
    * @param Phrase
    */ 
    public static void xml(Phrase phr) {
        Part p = new Part();
        p.addPhrase(phr);
        Score s = new Score("Score of "+ phr.getTitle());
        s.addPart(p);
        xml(s, phr.getTitle()+".xml");
    }
    
    /**
    * Save the jMusic Phrase as a xml file
    * @param Phrase
    * @param String filename
    */ 
    public static void xml(Phrase phr, String fileName) {
        Part p = new Part();
        p.addPhrase(phr);
        Score s = new Score("Score of "+ phr.getTitle());
        s.addPart(p);
        xml(s, fileName);
    }
    
    /**
    * Save the jMusic CPhrase as a xml file
    * Use the cphrase title as a the filename
    * @param CPhrase
    */ 
    public static void xml(CPhrase cphr) {
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        xml(s, cphr.getTitle()+".xml");
    }
    
    /**
    * Save the jMusic CPhrase as a xml file
    * @param CPhrase
    * @param String filename
    */ 
    public static void xml(CPhrase cphr, String fileName) {
        Part p = new Part();
        p.addCPhrase(cphr);
        Score s = new Score( "Score of "+ cphr.getTitle());
        s.addPart(p);
        xml(s, fileName);
    }
	
	//------------------------------------------------------------
	// Adjust score for tempo changes
	//------------------------------------------------------------
	private static Score adjustTempo(Score score){
		//Score score = scr.copy();	
		Enumeration enumParts = score.getPartList().elements();
		double scrTempo = 60.0/score.getTempo();
		while(enumParts.hasMoreElements()){
			Part part = (Part)enumParts.nextElement();
			double partTempo = scrTempo;
			if(part.getTempo() != 0.0)partTempo = 60.0/part.getTempo();
			Enumeration enumPhrases = part.getPhraseList().elements();
			while(enumPhrases.hasMoreElements()){
				Phrase phrase = (Phrase)enumPhrases.nextElement();
				Enumeration enumNotes = phrase.getNoteList().elements();
				while(enumNotes.hasMoreElements()){
					Note note = (Note)enumNotes.nextElement();
					note.setRhythmValue(note.getRhythmValue()*partTempo);
					note.setDuration(note.getDuration()*partTempo);
				}
			}
		}
		return score;
	}
}
