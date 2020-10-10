/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.World;
import gamenode.EditorGameNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Deneyr
 */
public class Object2DEditorFactory {
    
    private String className;
    
    private List<Object> listArgument;
    
    private Object2D template;
    
    public Object2DEditorFactory(String className){
        this.className = className;
        
        this.listArgument = new ArrayList<Object>();
    }
    
    public void addArgument(String argument){
        
        argument = argument.trim();
        
        if(argument.endsWith("f")){
            this.listArgument.add((float) Float.parseFloat(argument.substring(0, argument.length() - 1)));
        }else if(argument.endsWith("d")){
            this.listArgument.add((double) Double.parseDouble(argument.substring(0, argument.length() - 1)));
        }else if(argument.endsWith("i")){
            this.listArgument.add((int) Integer.parseInt(argument.substring(0, argument.length() - 1)));
        }else if(argument.toLowerCase().equals("true")){
            this.listArgument.add(true);
        }else if(argument.toLowerCase().equals("false")){
            this.listArgument.add(false);
        }else{
            this.listArgument.add(argument);
        }       
    }
    
    public Object2D createObject2D(World world, float posX, float posY){

        try {
            Class<?> act = Class.forName(this.className);

            List<Class<?>> argumentClassList = new ArrayList<Class<?>>();
            
            
            List<Object> cloneArgument = new ArrayList<Object>(this.listArgument);
            int i = 0;
            for(Object obj : cloneArgument){
                Class<?> actArgument = Object.class;
                try{
                    actArgument = Class.forName(obj.toString());
                    this.listArgument.set(i, null); 
                }
                catch (ClassNotFoundException e)
                {
                    if(obj instanceof String){
                        if(obj.equals("W")){
                            actArgument = World.class;
                            
                            this.listArgument.set(i, world); 
                        }else if(obj.equals("X")){
                            actArgument = float.class;
                            
                            this.listArgument.set(i, posX); 
                        }else if(obj.equals("Y")){
                            actArgument = float.class;
                            
                            this.listArgument.set(i, posY); 
                        }else{
                            actArgument = obj.getClass();
                        }
                    }else if(obj instanceof Float){
                        actArgument = float.class;
                    }else if(obj instanceof Double){
                        actArgument = double.class;
                    }else if(obj instanceof Integer){
                        actArgument = int.class;
                    }else if(obj instanceof Boolean){
                        actArgument = boolean.class;
                    }else{
                        actArgument = obj.getClass();
                    }
                }
                
                argumentClassList.add(actArgument);
                i++;
            }          
            
            Class<?>[] argumentClassArray = new Class<?>[argumentClassList.size()];
            i = 0;
            for(Class<?> argumentClass : argumentClassList){
                argumentClassArray[i] = argumentClass;  
                i++;
            } 
            
            Object[] argumentArray = new Object[this.listArgument.size()];
            i = 0;
            for(Object obj : this.listArgument){
                argumentArray[i] = obj;  
                i++;
            } 
            
            Constructor<?> constructor = act.getConstructor(argumentClassArray);

            return (Object2D)constructor.newInstance(argumentArray);
          
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(EditorGameNode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Object2DEditorFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Object2DEditorFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(Object2DEditorFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Object2DEditorFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    
    public void createTemplate(World world){
        this.template = this.createObject2D(world, 0, -100000f);
    }

    /**
     * @return the template
     */
    public Object2D getTemplate() {
        return template;
    }
    
    @Override
    public String toString(){
        String result = this.className + "\n";
        for(Object obj : this.listArgument){
            if(obj != null){
                result += "value : " + obj.toString() + " - type : " + obj.getClass() + "\n";
            }else{
                result += "value : null - type : NULL POINTER\n";
            }
        }
        return result;
    }
}
