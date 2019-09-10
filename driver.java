import java.util.Scanner;
import java.io.*;

public class driver {//I used three extra suggestion methods

	static Scanner scan= new Scanner(System.in);
	static QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<String>(); //dictionary
	static QuadraticProbingHashTable<Word> misspelledTable = new QuadraticProbingHashTable<Word>(); //misspelled words
	static boolean cont = true;
	static boolean sameFile = true;
	
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		
		String inputFile = args[0]; 
		dictionaryHash(inputFile); 
		while(cont == true) {
			Scanner scan = new Scanner(System.in);
			System.out.println("Please enter a file to spell check");
			String fileName = scan.nextLine();
			
			Scanner fileReader = new Scanner(new File(fileName));
			while(sameFile == true) {
				
				System.out.println("Print words (p), enter new file (f), or quit (q) ?");
				String choice = scan.nextLine(); 
				
				if(choice.equals("f")) { //choose new file
					//makes sure that there are no remaining misspelled words in the file
					if(fileReader.hasNext() || fileReader.hasNextLine()) {
						while(fileReader.hasNextLine()) {
							while(fileReader.hasNext()) {
								String test = fileReader.next();
								test = strip(test);
								if(!dictionary.contains(test)) {
									Word tmp = new Word(test);
									if(!misspelledTable.contains(tmp)) {
										misspelledTable.insert(tmp);
									}
								}
							}
							fileReader.nextLine();
						}
					}
					outfile(fileName, correctedFile(fileName), orderFile(fileName)); //writes files
					misspelledTable.makeEmpty(); //empties hash table for next file
					
					break;
				}
				else if(choice.equals("q")) { //quit
					if(fileReader.hasNext() || fileReader.hasNextLine()) { //makes sure there are no remaining misspelled words in the file
						while(fileReader.hasNextLine()) {
							while(fileReader.hasNext()) {
								String test = fileReader.next();
								test = strip(test);
								if(!dictionary.contains(test)) {
									Word tmp = new Word(test);
									if(!misspelledTable.contains(tmp)) {
										misspelledTable.insert(tmp);
									}
								}
							}
							fileReader.nextLine();
						}
					}
					cont = false;
					outfile(fileName, correctedFile(fileName), orderFile(fileName)); //writes file
					
					System.out.println("Goodbye!");
					break;
				}
				else { //print words
					int line = 1;
					boolean noQuit = true;
					
					while(fileReader.hasNextLine() && noQuit) {
						String[] words = fileReader.nextLine().split(" ");
						
						
						for(int i=0;i<words.length;i++)  {
							String test = words[i];
							
							
							test = strip(test);
							
							if(!dictionary.contains(test)) { //word not in dictionary
								Word tmp = new Word(test);
								if (!misspelledTable.contains(tmp)) { //word not in misspelled hash table
									misspelledTable.insert(tmp);
									
									System.out.println("-- "+test+" "+line);
									System.out.println("ignore all (i), replace all (r), next (n), or quit(q)");
									String decision =scan.nextLine(); 
									if (decision.equals("i")) {
										ignored(misspelledTable.get(tmp));
										
									}
									else if(decision.equals("r")) {
										replaceAll(misspelledTable.get(tmp));
										String s = scan.nextLine();
										if(!s.equals("n")) {
											int f = Integer.parseInt(s);
											misspelledTable.get(tmp).setReplacement(misspelledTable.get(tmp).getSuggestions().get(f-1));
											
										}
										
									}	
									else if(decision.equals("q")) { //quit 
										noQuit = false;
										
										for(int j=i;j<words.length;j++) { //finishes off the remaining strings on the current line
											String quit = words[j];
											quit = strip(quit);
											if(!dictionary.contains(quit)) {
												Word t = new Word(quit);
												if(!misspelledTable.contains(t)) {
													misspelledTable.insert(t);
												}
											}
											
										}
										break;
									}
								}
								else{ //if word is already in table
									if(misspelledTable.get(tmp).getIgnored()==false && misspelledTable.get(tmp).getReplacement().equals("")) {
										System.out.println("-- "+test+" "+line);
										System.out.println("ignore all (i), replace all (r), next (n), or quit(q)");
										String decision = scan.nextLine(); 
										if (decision.equals("i")) {
											ignored(misspelledTable.get(tmp));
											
										}
										else if(decision.equals("r")) {
											replaceAll(misspelledTable.get(tmp));
											String s = scan.nextLine();
											if(!s.equals("n")) {
												int f = Integer.parseInt(s);
												misspelledTable.get(tmp).setReplacement(misspelledTable.get(tmp).getSuggestions().get(f-1));
												
											}
											
										}
										else if(decision.equals("q")) { //quit
											noQuit = false;
											for(int j=i;j<words.length;j++) { //finishes off the remaining strings on the current line
												String quit = words[j];
												quit = strip(quit);
												if(!dictionary.contains(quit)) {
													Word t = new Word(quit);
													if(!misspelledTable.contains(t)) {
														misspelledTable.insert(t);
													}
												}
												
											}
											break;
										}
									}
								}
							}
								
							
						}
						 
							line ++;
							
							
						
						
						
					}
					if(fileReader.hasNext() || fileReader.hasNextLine()) {//inserts remaining missspelled words into hash table
						while(fileReader.hasNextLine()) {
							while(fileReader.hasNext()) {
								
								String test = fileReader.next();
								test = strip(test);
								if(!dictionary.contains(test)) {
									Word tmp = new Word(test);
									if(!misspelledTable.contains(tmp)) {
										misspelledTable.insert(tmp);
									}
								}
							}
							if(fileReader.hasNextLine()) {
								fileReader.nextLine();
							}
							
						}
					}
					System.out.println("Spell check complete!");
					
				}
			}
		}
		
		
	}
	
	
	public static void dictionaryHash(String NameOfFile) throws IOException{ //imports dictionary into Hash Table
		File file1 = new File(NameOfFile);
		Scanner filereader = new Scanner(file1);
		 
		System.out.println("Reading in Dictionary...");
		while(filereader.hasNextLine()) {
			String f = filereader.nextLine();
			dictionary.insert(f);
		}
		System.out.println("Dictionary Read.");
	}
	
	public static void ignored(Word w) {//sets word to ignored
		w.setIgnored(true);
	}
	
	public static void createSuggestions(Word w) {//creates suggestions for replacing a word
		String s = w.getWord();
		swap(w,s);
		delete(w,s); 
		replace(w,s); 
		insert(w,s);
		capitalize(w,s);
		
	}
	
	public static void swap(Word w, String s) { //swaps adjacent letters
		for(int i = 0; i<s.length()-1;i++) {
			char[] tmp = s.toCharArray();
			char first = tmp[i]; 
			tmp[i] = tmp[i+1];
			tmp[i+1] = first; 
			String n = ""; 
			for(int j = 0; j<tmp.length;j++) {
				n+= tmp[i]; 
			}
			if(dictionary.contains(n)) {
				w.getSuggestions().add(n); 
			}
			
			
		}
	}
	
	public static void delete(Word w, String s) { //deletes each character
		for (int i = 0;i<s.length();i++) {
			char[] tmp = s.toCharArray(); 
			String n = "";
			for (int j = 0;j<tmp.length;j++) {
				if(j!=i) {
					n += tmp[j]; 
				}
			}
			if(dictionary.contains(n)) {
				w.getSuggestions().add(n);
			}
			
			
		}
			
	}
	
	public static void replace(Word w, String s){ //replaces each character with characters 'a' - 'z'
		
		for(int i = 0; i<s.length();i++) {
			char[] tmp = s.toCharArray();
			for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
				tmp[i] = alphabet;
				String n = "";
				for(int j = 0;j<tmp.length;j++) {
					n += tmp[j];
				}
				if(dictionary.contains(n)) {
					w.getSuggestions().add(n);
				}
			}
		}
	}
	
	public static void insert(Word w, String s){ //inserts character a-z between every adjacent pair of letters and at the beginning and end
		for(char alphabet= 'a'; alphabet<='z';alphabet++) {
			String n = s;
			n = alphabet + s;
			if(dictionary.contains(n)) {
				w.getSuggestions().add(n); 
			}
		}
		

		for(int i = 0;i<s.length()-1;i++) {
			String beg = s.substring(0, i+1);
			String end = s.substring(i+1,s.length());
			for(char alphabet='a';alphabet<='z';alphabet++) {
				String n = "";
				n = beg+alphabet+end;
				if(dictionary.contains(n)) {
					w.getSuggestions().add(n);
				}
			}
			
			
		}
		for(char alphabet = 'a';alphabet<='z';alphabet++) {
			String n = s;
			n = s+alphabet;
			if(dictionary.contains(n)) {
				w.getSuggestions().add(n);
			}
					
		}
	}
	
	public static void capitalize(Word w, String s){ //capitalizes the string
		String n = s;
		n = s.toLowerCase();
		n = n.substring(0,1).toUpperCase() + n.substring(1,n.length()).toLowerCase();
		if(dictionary.contains(n)) {
			w.getSuggestions().add(n);
		}
	}
	
	public static void replaceAll(Word w) { //prints out the suggestions for a word
		createSuggestions(w); 
		if(w.getSuggestions().isEmpty()) {
			System.out.println("There are no suggestions, next (n)");
		}
		else {
			System.out.print("Replace with ");
			for(int i = 0;i<w.getSuggestions().size();i++) {
				int wordNum = i+1;
				System.out.print("("+wordNum+")"+w.getSuggestions().get(i)+" ");
			}
			System.out.print("or next (n)");
		}
		
	}
	public static String strip(String s) { //strips the String of any punctuation marks
		while(!Character.isLetter(s.charAt(0))) {
			 s = s.substring(1);
		}
		while(!Character.isLetter(s.charAt(s.length()-1))) {
			s = s.substring(0,s.length()-1);
		}
		return s;
	}
	
	
	
	public static void outfile(String n, String x,String y) throws IOException{ //creates two files, one that lists all the misspelled words in order and the other prints the entire file again with the replaced words
		BufferedWriter out = new BufferedWriter(new FileWriter(x));
		BufferedWriter out2 = new BufferedWriter(new FileWriter(y));
		Scanner fileReader = new Scanner(new File(n));
		int line = 1;
		while(fileReader.hasNextLine()) {
			String[] words = fileReader.nextLine().split(" ");
			
			
			for(int i=0;i<words.length;i++)  {
				String test = words[i];
				String check = strip(test);
				if(!dictionary.contains(check)) {
					out2.write(check);
					out2.write(" "); 
					out2.write(Integer.toString(line));
					out2.newLine(); 
					Word tmp = new Word(check);
					if (misspelledTable.contains(tmp)) {
						
						if(!misspelledTable.get(tmp).getReplacement().equals("")){
							String finish = test.replaceFirst(check, misspelledTable.get(tmp).getReplacement());
							out.write(finish);
							out.write(" ");
						
						}
					
						else {
							out.write(test);
							out.write(" ");
						
						}
					}
					
					
				}
				else {
					out.write(test);
					out.write(" ");
					
				}
				
			}
			line++;
			
			out.newLine();
		}
		out.close();
		out2.close();
	}
	
	public static String correctedFile(String s) { //creates the string for the name of one file
		String n = s;
		if(s.length() >= 5) {
			if(s.substring(s.length()-4, s.length()).equals(".txt")) {
				 n = s.substring(0,s.length()-4);
			}
		}
		n += "_corrected.txt";
		return n;
	}
	
	public static String orderFile(String s) { //creates the string for the name of the other file
		String n = s;
		if(s.length() >= 5) {
			if(s.substring(s.length()-4, s.length()).equals(".txt")) {
				 n = s.substring(0,s.length()-4);
			}
		}
		n += "_order.txt";
		return n;
	}
	
}
