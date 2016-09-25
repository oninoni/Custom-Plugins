package de.oninoni.OnionPower.Machines.DispenserBased;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Jukebox;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import de.oninoni.OnionPower.Items.Batrod;
import de.oninoni.OnionPower.Items.CustomsItems;
import de.oninoni.OnionPower.Machines.MachineDispenser;
import de.oninoni.OnionPower.Machines.MachineManager;
import de.oninoni.OnionPower.Machines.Upgrades.Upgrade;

public class Miner extends MachineDispenser{

	private int idleTimer = 0;
	
	private static final int MAXRANGE = 128;
	
	public Miner(Location position, MachineManager machineManager, int power, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, power, upgrades);
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			@Override
			public void run() {
				dispenser.getInventory().setItem(0, CustomsItems.getGlassPane((byte) 2, "§4§kSecret§r §4§kMessage"));
				dispenser.getInventory().setItem(1, powerCore);
				dispenser.getInventory().setItem(2, CustomsItems.getGlassPane((byte) 2, "§4§kLol§r §4§kROFL"));
				//dispenser.getInventory().setItem(4, arg1);
				dispenser.getInventory().setItem(3, CustomsItems.getGlassPane((byte) 2, "§4§kZweiundvierzig§r"));
				dispenser.getInventory().setItem(5, CustomsItems.getGlassPane((byte) 2, "§4§kIch§r §4§kmag§r §4§kZüge"));
				dispenser.getInventory().setItem(6, CustomsItems.getGlassPane((byte) 2, "§4§k42§r §4§k256"));
				dispenser.getInventory().setItem(7, CustomsItems.getMinerPickAxe());
				dispenser.getInventory().setItem(8, CustomsItems.getGlassPane((byte) 2, "§4§kPossseidon§r §4§kstinkt!"));
				
				reOpenInventories();
			}
		}, 1L);
		SetupPowerIO();
	}
	
	public Miner(Location position, MachineManager machineManager, HashMap<Integer, Upgrade> upgrades) {
		super(position, machineManager, upgrades);
		SetupPowerIO();
	}
	
	private void SetupPowerIO(){
		for(int i = 0; i < 6; i++){
			allowedOutputs[i] = false;
		}
	}
	
	@Override
	protected void setCoreSlot() {
		coreSlot = 1;
	}

	@Override
	public String getDisplayName() {
		return "§6§lElectrical Stripminer";
	}

	@Override
	public int getMaxPowerOutput() {
		return 0;
	}

	@Override
	public int getMaxPowerInput() {
		return 500;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void updateBlock() {
		requestFromConnected();
		if(idleTimer <= 0){
			Vector direction = directions[directionAdapter[dispenser.getRawData()]];
			int i;
			Block targetInventoryBlock = position.clone().subtract(direction).getBlock();
			if(targetInventoryBlock.getState() instanceof InventoryHolder){
				InventoryHolder targetInventory = (InventoryHolder) targetInventoryBlock.getState();
				for(i = 1; i <= MAXRANGE; i++){
					Location newPos = position.clone();
					newPos.add(direction.clone().multiply(i));
					Block b = newPos.getBlock();
					if(b!= null && b.getType() != Material.AIR){
						if(b.getType() == Material.BEDROCK)break;
						if(b.getType() == Material.WATER || b.getType() == Material.STATIONARY_WATER)continue;
						if(b.getType() == Material.LAVA || b.getType() == Material.STATIONARY_LAVA)continue;
						if(b.getState() instanceof InventoryHolder || b.getState() instanceof Jukebox){
							Bukkit.broadcastMessage(""+((InventoryHolder)b.getState()).getInventory().getItem(0));
							continue;
						};
						double distance = position.distance(newPos);
						if(getPower() > 5 * distance){
							Collection<ItemStack> drops = b.getDrops();
							for (ItemStack itemStack : drops) {
								targetInventory.getInventory().addItem(itemStack);
							}
							MinerShot shot = new MinerShot(this, b);
							Runnable r = new Runnable() {
								@Override
								public void run() {
									power -= 5;
									if(shot.update())return;
									Bukkit.getScheduler().runTaskLater(plugin, this, 1L);
								}
							};
							idleTimer = i / 2 + 1;
							Bukkit.getScheduler().runTaskLater(plugin, r, 1L);
						}else{
							idleTimer = 20;
						}
						break;
					}
				}
				if(i > MAXRANGE){
					idleTimer = 20;
				}
			}
		}else{
			idleTimer--;
		}
		
	}
	
	@Override
	public void onMoveFrom(InventoryMoveItemEvent e) {
		e.setCancelled(true);
	}
	
	@Override
	public void onClick(InventoryClickEvent e) {
		super.onClick(e);
		e.setCancelled(true);
	}
	
	@Override
	protected void resetItemAt(int id) {
		if(id == 1){
			ItemStack batrod = Batrod.create();
			Batrod.setPower(batrod, getPower());
			dispenser.getInventory().setItem(id, batrod);
		}else if(id == 4){
			dispenser.getInventory().setItem(id, new ItemStack(Material.REDSTONE_BLOCK));
		}else if(id == 7){
			dispenser.getInventory().setItem(id, new ItemStack(Material.IRON_PICKAXE));
		}else{
			dispenser.getInventory().setItem(id, new ItemStack(Material.AIR));
		}
	}
	
	@Override
	protected boolean doesExplode() {
		return false;
	}

	@Override
	protected void spawnDesignEntities() {
		// TODO Auto-generated method stub
		
	}
}
