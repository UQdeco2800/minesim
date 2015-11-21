package minesim.entities;

public interface HasSkills {
    
    /**
     * Get the level of the tradeSkill
     * @return
     */
    double getTradeSkill();
    
    /**
     * Set the level of tradeSkill
     * @param skill
     */
    void setTradeSkill(double skill);
    
    /**
     * Get the level of intelligence 
     * @return
     */
    double getIntelligenceSkill();
    
    /**
     * Set level of intelligence
     * @param skill
     */
    void setIntelligenceSkill(double skill);
    
    /**
     * Get the level of fighting skill ability
     * @return
     */
    double getFightSkill();
    
    /**
     * Set the level of fighting skill
     * @param skill
     */
    void setFightSkill(double skill);
}
