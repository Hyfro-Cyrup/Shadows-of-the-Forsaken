/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package shadows.of.the.forsaken;

/**
 *
 * @author Son Nguyen - Thanks Nathan for starting the stuff
 * Probably make more stuff 'final' in general. 
 */
abstract class Entity {
    private String Name; // name for UI
    private String description; // Entity Description
    
    // referene to name of sprite/image file to give to UI classes.
    private String spriteReference; 
    
    Entity(String name, String desc, String spriteFileName){
        this.Name = name;
        this.description = desc; 
        this.spriteReference = spriteFileName;
    }
      
    public String getName(){
        return Name;
    }

    public String getSpriteReference(){
        return spriteReference;
    }
    
}
