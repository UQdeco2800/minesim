package minesim.buffs;

import minesim.entities.WorldEntity;

/**
 * Abstract Class that is unique, it holds all the essentials for a buff or 
 * debuff
 *
 * @author Team Cook (Chavez, Miller, Teed, Winter)
 */
public abstract class Buff implements HasName {
	
	/* max cooldown */
	protected int cooldownmax = 0;
	/* building information */
    protected String name = "";
    protected String desc = "";
    
    /* buff image string */
    protected String image = "BuffImages/MissingSprite.png";
    
    /* Time left */
    protected int buffCooldown = 0;
    
    public Buff() {
    	
    }
    
    /**
     * buffTick is what the buff will do to it's owner entity onTick
     * @param sender the entity effected by the buff
     */
    public void buffTick(WorldEntity sender) {

    	/* initial buff effect e.g temporary increase to strength*/
    	if (this.buffCooldown == cooldownmax) {
    		
			startBuff(sender);
		}
    	
		this.buffCooldown -= 1;
		
		/* override continuous effects here in your own class */
		
		
		/* final buff effect  e.g temporary increase to strength removed*/
		if (this.buffCooldown == 0) {
			removeBuff(sender);
		}
		
	}
    
    /**
     * This is a starting effect function which will change the entity's x,y or 
     * z
     * @param sender the entity effected by the buff
     */
    public abstract void startBuff(WorldEntity sender);
    
    /**
     * This is a ending effect function which will change the entity's x,y or 
     * z. It is usually used to remove the initial boost that should not be
     * permanent
     * @param sender the entity effected by the buff
     */
    public abstract void removeBuff(WorldEntity sender);
    
    /* if you ever need to set the coolDown */
    public void setCooldown(int factor) {
    	buffCooldown = factor;
    }
    
    /* get the current int cooldown */
    public int getCooldown() {
    	return buffCooldown;
    }
    
    /* has name interface */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    /* has name interface */
    
    /* get buff description   */
    public String getDesc() {
        return this.desc;
    }
    /* set buff description   */
    public void setDesc(String name) {
        this.desc = name;
    }
    
    /* get image */
    public String getImageUrl() {
    	return this.image;
    }
    
    public void setImageUrl(String url) {
    	this.image = url;
    }	

}
