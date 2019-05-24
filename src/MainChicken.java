//import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
//import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(name = "Chicken slayer", author = "dokato", version = 1.1, info = "", logo = "")
public class MainChicken extends Script{
	
	private HashSet<NPC> chickenSet;
	private long timeBegan;
	private long timeRan;
	private long timeBotted;
	private long timeOffline;
	private String status;
	//private int str,def,attk;
	//private boolean toCheck;
	
	public void onStart(){
		this.timeBegan = System.currentTimeMillis();
		this.timeBotted = 0;
		this.timeOffline = 0;
		chickenSet = new HashSet<>();
	}
	
	public int onLoop() throws InterruptedException{
		status="loop started";
		if(getClient().isLoggedIn()){
			//getLvls();
			procedures();
			if(getMap().canReach(new Position(3180,3292,0))){
				//rightStyle();
				fightChickens();
			}else{
				status="Trying to open the gate";
				getObjects().closest(new Area(3178,3290,3183,3287),"Gate").interact("Open");
				sleep(random(300,600));
			}
		}
		status="loop ended";
		return 0;
	}
	
	public void onPaint(Graphics2D g1){
		this.timeRan = (System.currentTimeMillis() - this.timeBegan);
		if (getClient().isLoggedIn()) {
			this.timeBotted = (this.timeRan - this.timeOffline);
		} else {
			this.timeOffline = (this.timeRan - this.timeBotted);
		}
		
		Graphics2D g = g1;
		
		g.setFont(new Font("Arial", 0, 13));
		g.setColor(new Color(255, 255, 255));
		g.drawString("Version: " + getVersion(), 20, 50);
		g.drawString("Runtime: " + ft(this.timeRan), 20, 65);
		g.drawString("Time botted: " + ft(this.timeBotted), 20, 80);
		g.drawString("Status: " + this.status, 20, 95);
		
		g.drawString("Attk: " + getSkills().getStatic(Skill.ATTACK), 20,115 );
		g.drawString("" + getSkills().experienceToLevel(Skill.ATTACK), 20,130 );
		
	    g.drawString("Str: " + getSkills().getStatic(Skill.STRENGTH), 20, 160);
	    g.drawString("" + getSkills().experienceToLevel(Skill.STRENGTH), 20, 175);
	    
	    
	    g.drawString("Def: " + getSkills().getStatic(Skill.DEFENCE), 20, 205);
	    g.drawString("" + getSkills().experienceToLevel(Skill.DEFENCE), 20, 220);
	    
	    g.drawString("Hp: " + getSkills().getStatic(Skill.HITPOINTS), 20, 250);
	    g.drawString("" + getSkills().experienceToLevel(Skill.HITPOINTS), 20, 265);
	}
	
	/*private void getLvls(){
		
	}*/
	
	/*private void rightStyle() throws InterruptedException{
		if(getWidgets().isVisible(593,27)){
			String style = getSwapStyle();
			if(style.equals("str")){
				if(!getColorPicker().isColorAt(652,274,new Color(67,16,15))){
					getMouse().click(random(657,714),random(253,292),false);
					sleep(random(100,200));
				}
			}else if(style.equals("attk")){
				if(!getColorPicker().isColorAt(568,271,new Color(67,16,15))){
					getMouse().click(random(572,633),random(253,292),false);
					sleep(random(100,200));
				}
			}else if(style.equals("def")){
				if(!getColorPicker().isColorAt(652,318,new Color(67,16,15))){
					getMouse().click(random(657,714),random(306,343),false);
					sleep(random(100,200));
				}
			}
			
			
		}else{
			getTabs().open(Tab.ATTACK);
			sleep(random(100,200));
		}
	}*/
	
	/*private String getSwapStyle(){
		int str=getSkills().getStatic(Skill.STRENGTH);
		int attk=getSkills().getStatic(Skill.ATTACK);
		int def=getSkills().getStatic(Skill.DEFENCE);
		if((((str>=10&&attk<10)||(str>=20&&attk<20))||(str>=30&&attk<30))||(str>=40&&attk<40)){
			return "attk";
		}else if((((attk>=10&&str<10)||(attk>=20&&str<20))||(attk>=30&&str<30))||(attk>=40&&str<40)){
			return "str";
		}else if((attk>=40&&str>=40)&&def<40){
			return "def";
		}else if((attk>=40&&str>=40)&&def<40){
			stop();
		}
		return null;
	}*/
	
	private void fightChickens() throws InterruptedException{
		if(((!myPlayer().isUnderAttack()&&!myPlayer().isAnimating())&&!getCombat().isFighting())&&!myPlayer().isMoving()){
			status="about to get npcList";
			List<NPC> npcsList = getNpcs().getAll();
			status="just getted npcList";
			for(NPC npc : npcsList){
				status="just entered for loop";
				if(npc.getName().equals("Chicken")){
					status="it is a chicken";
					if(((!npc.isUnderAttack()&&npc.isAttackable())&&map.canReach(npc.getPosition()))&&npc.getHealth()!=0){
						status="about to add to chickenSet";
						chickenSet.add(npc);
						status="just added to chickenSet";
					}
				}
			}
			NPC chicken = npcs.closest(chickenSet);
			if(!chicken.isAnimating() || chicken.isMoving()){
			status="about to attack the chicken";
				chicken.interact("Attack");
				sleep(random(300,600));
			}
		}
		status="about to clear the chickenSet";
		chickenSet.clear();
		status="just cleared the chickenSet";
	}
	
	/*private void fightChickens(){
		@SuppressWarnings("unchecked")
		NPC chicken = getNpcs().closest(new Filter<NPC>({
		    @Override
		    public boolean match(NPC npc){
		       return npc.getName().equalsIgnoreCase("chicken") && !npc.isUnderAttack() && getMap().canReach(npc) && npc.getHealth() > 0;
		    }
		});
	}*/
	
	private void procedures() throws InterruptedException{
		getCamera().toTop();
		if(getInventory().isItemSelected()){
			getInventory().deselectItem();
			sleep(random(200,400));
		}
		if(getSettings().getRunEnergy()>random(7,14)){
			getSettings().setRunning(true);
			sleep(random(200,400));
		}
	}
	
	private String ft(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(duration));
		if (days == 0L) {
			res = hours + ":" + minutes + ":" + seconds;
		} else {
			res = days + ":" + hours + ":" + minutes + ":" + seconds;
		}
		return res;
	}
}
