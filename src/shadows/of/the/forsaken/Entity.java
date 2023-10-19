/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 *
 * @author Son Nguyen
 * Probably make more stuff 'final' in general. 
 */
abstract class Entity {
    private String Name; // name for UI
    
    // referene to name of sprite/image file to give to UI classes.
    private String spriteReference; 
    
    
    Entity(String name, String spriteFileName){
        this.Name = name;
        this.spriteReference = spriteFileName;
    }
      
    public String getName(){
        return Name;
    }

    public String getSpriteReference(){
        return spriteReference;
    }
    
}
