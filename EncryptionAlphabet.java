/* EncryptionAlphabet class
  * Description: Create a program that generates a ciphered alphabet. User's inputted message can be decrpyt and
  * encrypted. Store the ciphered alphabet in a file.
  * Author: Preston
  * Version 1.0 Nov 18. 2020
  */

// Import Scanner and java.io classes
import java.util.Scanner;
import java.io.*; // imports entire io class

public class EncryptionAlphabet {
    public static void main(String [] args) throws IOException {
        
        // File variables
        final boolean APPEND = true;
        File preMadeDocument = new File("/Users/33571/Java 11/EncryptionPreMadeDocument.txt");
        FileWriter generateDocument = new FileWriter("/Users/33571/Java 11/EncryptionGenerateDocument.txt", APPEND);
        // File that keeps track of all hidden/encrypted messages
        FileWriter hiddenDocument = new FileWriter("/Users/33571/Java 11/EncryptionHiddenDocument.txt", APPEND);
        // File that keeps track of all revealed/decrypted messages
        FileWriter revealDocument = new FileWriter("/Users/33571/Java 11/EncryptionRevealDocument.txt", APPEND);
        // To read decrypted mesages
        File readRevealDocument = new File("/Users/33571/Java 11/EncryptionRevealDocument.txt");
        Scanner inputReveal = new Scanner(readRevealDocument);
        // To read decrypted mesages
        File readHiddenDocument = new File("/Users/33571/Java 11/EncryptionHiddenDocument.txt");
        Scanner inputHidden = new Scanner(readHiddenDocument);
        // To write to files
        PrintWriter outputHidden = new PrintWriter(hiddenDocument);
        PrintWriter outputReveal = new PrintWriter(revealDocument);
        String cipherFileName;
        
        // Create scanner to read inputs from keyboard
        Scanner keyboard = new Scanner(System.in);
        
        // CONSTANTS AND VARIABLES
        String message, decodedMessage,codedMessage;
        
        // Include both upper and lower case in alphabet
        String originalAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String cipherAlphabet = "ZXCVBNMLKJHGFDSAQWERTYUIOP";
        
        // Stores messages
        String secretMessage, unsecretMessage;
        String oldRevealMessage, oldHiddenMessage;
        
        // Boolean constant values
        final int YES = 1;
        final int NO = 0;
        
        // Flag variables
        int typeOfCipher;
        int continueProgramStatus = YES;
        int translationDecision;
        int cipherSelection;
        int storeNewFile;
        int viewOld;
        
        // If user chooses to continue program
        while(continueProgramStatus == YES) {
          System.out.print("Do you want to use a pre-made ciphered alphabet [1] or generate your own [0]? ");
          typeOfCipher = keyboard.nextInt();
          keyboard.nextLine();
          // user decides to generate pre-made cipher
          if(typeOfCipher == YES) {
            // find out if there is next line in a file
            System.out.println("List of pre-made ciphered alphabets: \n" +
            "Enter [1] for " + readCipher(preMadeDocument, 1) + "\n" +
            "Enter [2] for " + readCipher(preMadeDocument, 2) + "\n" +
            "Enter [3] for " + readCipher(preMadeDocument, 3));
            cipherSelection = keyboard.nextInt();
            keyboard.nextLine();
            
            // Must select integer within correct range
            if(cipherSelection >= 1 && cipherSelection <= 3) {
              cipherAlphabet = readCipher(preMadeDocument, cipherSelection);
            } else {
              System.out.println("Failed to enter integer from 1-3. You got the default alphabet.");
            }
            System.out.println("Obtained your pre-made cipher alphabet.");
          } else if(typeOfCipher == NO) {
              // Generate random cipher alphabet
              cipherAlphabet = generateCipher(originalAlphabet, cipherAlphabet);
              System.out.println("Generated a cipher alphabet.");
              
              // Writes ciphered alphabet to cipher alphabet text file
              System.out.print("Do you want to store this cipherAlphabet in a new file? YES [1] NO [0]: ");
              storeNewFile = keyboard.nextInt();
              keyboard.nextLine();
              
              if(storeNewFile == YES) {
                System.out.print("Enter cipher file name: ");
                cipherFileName = keyboard.nextLine();
                writeNewCipherFile(cipherFileName, cipherAlphabet);
              } else if (storeNewFile == NO) {
                System.out.println("Ok, will store in default file - generateDocument");
                writeCipher(generateDocument, cipherAlphabet);
              } else {
                System.out.println("Failed to enter valid input. Will not create new file.");
              }
          } else {
            System.out.print("You failed to answer with 1 or 0. You got the default alphabet.");
          }
          System.out.println("Your cipher alphabet is: " + cipherAlphabet);
          // Ask user if they want to encrypt to decrypt
          System.out.print("Enter [1] to encrypt a message  OR  [0] to decrypt a message: ");
          translationDecision = keyboard.nextInt();
          keyboard.nextLine();
          
          if(translationDecision == YES) {
            System.out.println("Enter the message you want to encrypt: ");
            message = keyboard.nextLine();
            // Sends decrypted message to revealed message file
            outputReveal.println(message);
            secretMessage = encrypting(message, originalAlphabet, cipherAlphabet);
            message = secretMessage;
            // Sends encrypted message to hidden message file
            outputHidden.println(message);
            System.out.println("Your encrypted message is: " + message);
          } else if(translationDecision == NO) {
            System.out.println("Enter the message you want to decrypt: ");
            message = keyboard.nextLine();
            // Sends encrypted message to hidden message file
            outputHidden.println(message);
            unsecretMessage = decrypting(message, originalAlphabet, cipherAlphabet);
            message = unsecretMessage;
            // Sends decrypted message to revealed message file
            outputReveal.println(message);
            System.out.println("Your decrypted message is: " + message);
          } else {
            System.out.println("Failed to provide valid input...");
          }
          
          // Decision to continue program or not
          System.out.println("Do you want to encrypt/decrypt another message?");
          System.out.print("Enter [1] for YES  OR  [0] for NO: ");
          continueProgramStatus = keyboard.nextInt();
          keyboard.nextLine();
        } // end of while loop
        
        // Close objects
        outputHidden.close();
        outputReveal.close();
        
        // Ask user if they want to see old messages
          System.out.print("Do you want to view old messages? YES [1] NO [0]: ");
          viewOld = keyboard.nextInt();
          if(viewOld == YES) {
            System.out.println("Printing old decrypted messages:");
            while(inputReveal.hasNext()) {
              oldRevealMessage = inputReveal.nextLine();
              System.out.println(oldRevealMessage);
            }
            System.out.println("\nPrinting old encrypted messages:");
            while(inputHidden.hasNext()) {
              oldHiddenMessage = inputHidden.nextLine();
              System.out.println(oldHiddenMessage);
            }
          } else if (viewOld == NO) {
            System.out.println("Will not print old messages.");
          } else {
            System.out.println("Failed to provide valid answer. Will not print old messages.");
          }
          System.out.println("\nEnding program.");
    
    // Close read input objects
    inputHidden.close();
    inputReveal.close();
    keyboard.close();
    } // main method end
//----------------------------------------------------------
    // method that takes in message and hides it into an unreadable one
    public static String encrypting(String message, String originalAlphabet, String cipherAlphabet) {
      char letter, lowerLetter, upperLetter;
      int index;
      char hiddenLetter;
      String hiddenMessage = "";
      int asciiValue, hiddenAsciiValue, upperAsciiValue;
      for(int i = 0; i < message.length(); i++) {
        // finds letter of index i of the message
        letter = message.charAt(i);
        asciiValue = (int) letter;
        // if ascii value belongs to lowercase letter
        if(asciiValue >= 97 && asciiValue <=122) {
          // find ascii value of uppercase by adding 32
          upperAsciiValue = asciiValue - 32;
          // find uppercase letter
          upperLetter = (char) upperAsciiValue;
          // find index of uppercase letter in original alphabet
          index = originalAlphabet.indexOf(upperLetter);
          // find letter in cipher alphabet using same index
          hiddenLetter = cipherAlphabet.charAt(index);
          // convert to ascii
          hiddenAsciiValue = (int) hiddenLetter;
          // convert to lowercase
          hiddenAsciiValue += 32;
          // convert to letter
          lowerLetter = (char) hiddenAsciiValue;
          hiddenMessage += lowerLetter;
        } else {
          // finds index of the letter in the original alphabet
          index = originalAlphabet.indexOf(letter);
          // do not change character if not in original alphabet
          if(index == -1) {
            hiddenMessage = hiddenMessage + letter;
          } else {
            // finds the letter of the index of the cipher alphabet
            hiddenLetter = cipherAlphabet.charAt(index);
            hiddenMessage = hiddenMessage + hiddenLetter;
          }
        }
      } // end of for loop
      return hiddenMessage;
    } // encrypt method end
//----------------------------------------------------------
    // method that takes in unreadable message and decryptes it
    public static String decrypting(String message, String originalAlphabet, String cipherAlphabet) {
      // Variables
      char letter, lowerLetter, upperLetter;
      int index;
      char revealLetter;
      String revealMessage = "";
      int asciiValue, revealAsciiValue, upperAsciiValue;
      for(int i = 0; i < message.length(); i++) {
        // finds letter of index i of the message
        letter = message.charAt(i);
        asciiValue = (int) letter;
        // if ascii value belongs to lowercase letter
        if(asciiValue >= 97 && asciiValue <=122) {
          // find ascii value of uppercase by adding 32
          upperAsciiValue = asciiValue - 32;
          // find uppercase letter
          upperLetter = (char) upperAsciiValue;
          // find index of uppercase letter in cipher alphabet
          index = cipherAlphabet.indexOf(upperLetter);
          // find letter in original alphabet using same index
          revealLetter = originalAlphabet.charAt(index);
          // convert to ascii
          revealAsciiValue = (int) revealLetter;
          // convert to lowercase
          revealAsciiValue += 32;
          // convert to letter
          lowerLetter = (char) revealAsciiValue;
          revealMessage += lowerLetter;
        } else {
          // finds index of the letter in the original alphabet
          index = cipherAlphabet.indexOf(letter);
          // do not change character if not in original alphabet
          if(index == -1) {
            revealMessage = revealMessage + letter;
          } else {
            // finds the letter of the index of the cipher alphabet
            revealLetter = originalAlphabet.charAt(index);
            revealMessage = revealMessage + revealLetter;
          }
        }
      }
      return revealMessage;
    }
//----------------------------------------------------------
    public static String generateCipher (String originalAlphabet, String cipherAlphabet) {
      int upRanNum, upAsciiValue, upRandomAsciiValue;
      char upLetter, upRandomLetter;
      final int ALPHABET_LENGTH = 26;
      final int NOT_IN_ALPHABET = -1;
      cipherAlphabet = "";
      // loop to create uppercase string
      while(cipherAlphabet.length() < ALPHABET_LENGTH) {
        // generates 26 uppercase letters
        int k = 0;
        upRanNum = (int) (Math.random()*(ALPHABET_LENGTH-1+1)+1);
        // find character
        upLetter = originalAlphabet.charAt(k);
        // find ascii value of character
        upAsciiValue = (int) upLetter;
        // create random ascii value
        upRandomAsciiValue = upAsciiValue + upRanNum;
        // make sure ascii value is for uppercase letters
        if(upRandomAsciiValue > 90) {
          upRandomAsciiValue = upRandomAsciiValue - ALPHABET_LENGTH;
        }
        // convert back to character
        upRandomLetter = (char) upRandomAsciiValue;
        // If random letter is not in cipherAlphabet, add it to the alphabet
        if(cipherAlphabet.indexOf(upRandomLetter) == NOT_IN_ALPHABET) {
          cipherAlphabet = cipherAlphabet + upRandomLetter;
          k++;
        }
      } // end of uppercase while loop
      return cipherAlphabet;
    } // end of generateCipher method    
//----------------------------------------------------------
    public static String readCipher(File preMadeDocument, int cipherSelection) throws IOException {
      // prints user's chosen alphabet
      Scanner input = new Scanner(preMadeDocument);
      String getCipherAlphabet = "";
      for(int i = 1; i <=cipherSelection; i++) {
        getCipherAlphabet = input.nextLine();
      }
      return getCipherAlphabet;
    } // readCipher method end
//----------------------------------------------------------
    // To write cipher alphabet into new file
    public static void writeNewCipherFile(String cipherFileName, String cipherAlphabet) throws IOException {
      File newCipherFile = new File("/Users/33571/Java 11/" + cipherFileName + ".txt");
      PrintWriter outputToFile = new PrintWriter(newCipherFile);
      outputToFile.println(cipherAlphabet);
      outputToFile.close();
    } // writeNewCipherFile method end
//----------------------------------------------------------
    // to write cipher alphabet into same file
    public static void writeCipher(FileWriter generateDocument, String cipherAlphabet) throws IOException {
       PrintWriter output = new PrintWriter(generateDocument);
       output.println(cipherAlphabet);
       output.close();
     } // writeCipher method end
} // EncryptionAlphabet class end
