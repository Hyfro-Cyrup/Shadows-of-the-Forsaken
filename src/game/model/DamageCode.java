/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package game.model;

/**
 * Keeps track of specific negative damage values used to signify certain events
 * Almost an enum
 */
public class DamageCode 
{
    public static final int FOUGHT      = 0;
    public static final int DEFENDED    = -1;
    public static final int MISSED      = -2;
    public static final int RAN_AWAY_SUCCESSFUL = -3;
    public static final int RAN_AWAY_FAILED     = -4;
    public static final int INVESTIGATED        = -5;
    public static final int TOUCHED     = -6;
    
    private DamageCode(){}
}
