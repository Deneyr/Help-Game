/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guicomponents;

import guicomponents.GuiPortrait.Emotion;
import guicomponents.GuiPortrait.Character;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deneyr
 */
public class Dialogue extends ArrayList<String>{

    private List<Character> listCharactersLeft;
    private List<Character> listCharactersRight;
    private List<Emotion> listEmotionsLeft;
    private List<Emotion> listEmotionsRight;
    
    public int getNbReply(){
        return this.size();
    }
    
    public void addReply(String reply, Character leftChara, Emotion leftEmotion, Character rightChara, Emotion rightEmotion){
        this.listCharactersLeft.add(leftChara);
        this.listEmotionsLeft.add(leftEmotion);
        
        this.listCharactersRight.add(rightChara);
        this.listEmotionsRight.add(rightEmotion);
    }
    
    /**
     * @return the listCharacterRight
     */
    public Character getCharacterRight(int index) {
        return listCharactersRight.get(index);
    }

    /**
     * @return the listEmotionsLeft
     */
    public Emotion getEmotionLeft(int index) {
        return listEmotionsLeft.get(index);
    }

    /**
     * @return the listEmotionsRight
     */
    public Emotion getEmotionRight(int index) {
        return listEmotionsRight.get(index);
    }

    /**
     * @return the listCharacterLeft
     */
    public Character getCharacterLeft(int index) {
        return listCharactersLeft.get(index);
    }
}
