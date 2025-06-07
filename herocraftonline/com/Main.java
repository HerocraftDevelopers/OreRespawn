/*     */ package tfw.OR;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.GameMode;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.PluginManager;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ 
/*     */ public class Main
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*  23 */   HashMap<Block, Material> block = new HashMap<Block, Material>();
/*  24 */   HashMap<Block, Integer> timer = new HashMap<Block, Integer>();
/*     */   
/*     */   public void onEnable() {
/*  27 */     PluginManager pm = getServer().getPluginManager();
/*  28 */     pm.registerEvents(this, (Plugin)this);
/*  29 */     File config = new File(getDataFolder() + File.separator + "config.yml");
/*  30 */     if (!config.exists()) {
/*  31 */       getLogger().info("Generating config.yml");
/*  32 */       getConfig().addDefault("COAL_ORE", Integer.valueOf(10));
/*  33 */       getConfig().addDefault("IRON_ORE", Integer.valueOf(10));
/*  34 */       getConfig().addDefault("GOLD_ORE", Integer.valueOf(10));
/*  35 */       getConfig().addDefault("DIAMOND_ORE", Integer.valueOf(10));
/*  36 */       getConfig().addDefault("EMERALD_ORE", Integer.valueOf(10));
/*  37 */       getConfig().addDefault("LAPIS_ORE", Integer.valueOf(10));
/*  38 */       getConfig().addDefault("GLOWING_REDSTONE_ORE", Integer.valueOf(10));
/*  39 */       getConfig().options().copyDefaults(true);
/*  40 */       saveConfig();
/*     */ 
/*     */       
/*  43 */       getConfig().options().copyDefaults(true);
/*  44 */       saveConfig();
/*     */     } 
/*  46 */     BukkitRunnable task = new BukkitRunnable() {
/*     */         public void run() {
/*  48 */           for (Block b : Main.this.timer.keySet()) {
/*  49 */             int newtime = ((Integer)Main.this.timer.get(b)).intValue() + 1;
/*  50 */             Main.this.timer.put(b, Integer.valueOf(newtime));
/*  51 */             if (newtime >= Main.this.getTime(b))
/*     */             {
/*  53 */               if (b.getChunk().isLoaded()) {
/*     */                 
/*  55 */                 b.setType(Main.this.block.get(b));
/*  56 */                 Main.this.block.remove(b);
/*  57 */                 Main.this.timer.remove(b);
/*     */               } 
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     task.runTaskTimer((Plugin)this, 0L, 20L);
/*     */   }
/*     */   
/*     */   public void onDisable() {
/*  71 */     for (Block b : this.block.keySet()) {
/*  72 */       b.setType(this.block.get(b));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTime(Block b) {
/*  81 */     int time = getConfig().getInt(((Material)this.block.get(b)).toString());
/*  82 */     return time;
/*     */   }
/*     */   
/*     */   @EventHandler(ignoreCancelled = false)
/*     */   public void onMine(final BlockBreakEvent event) {
/*  87 */     if (event.getPlayer().getGameMode() == GameMode.CREATIVE)
/*  88 */       return;  if (event.isCancelled())
/*  89 */       return;  if (!getConfig().contains(event.getBlock().getType().toString())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  94 */     this.block.put(event.getBlock(), event.getBlock().getType());
/*  95 */     this.timer.put(event.getBlock(), Integer.valueOf(0));
/*  96 */     getServer().getScheduler().scheduleSyncDelayedTask((Plugin)this, new Runnable() {
/*     */           public void run() {
/*  98 */             if (event.isCancelled())
/*  99 */               return;  event.getBlock().setType(Material.BEDROCK);
/*     */           }
/* 101 */         },  2L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   public void onClick(PlayerInteractEvent event) {
/* 109 */     if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
/* 110 */       return;  Block block = event.getClickedBlock();
/* 111 */     if (block.getType() != Material.BEDROCK)
/* 112 */       return;  if (!this.timer.containsKey(block))
/* 113 */       return;  int left = getTime(block) - ((Integer)this.timer.get(block)).intValue();
/* 114 */     event.getPlayer().sendMessage(ChatColor.RED + "This block will respawn in " + left + " seconds!");
/*     */   }
/*     */ }


/* Location:              C:\Users\ETERNUS\Downloads\OreRespawn.jar!\tfw\OR\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */