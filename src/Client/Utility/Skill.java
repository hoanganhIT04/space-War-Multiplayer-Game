package Client.Utility;

public class Skill {

    private boolean available = true;
    private boolean using = false;
    private int cooldownTime, cdTimeLeft;
    private int useTimeLeft;
    public Skill(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public void useSkill(){
        available = false;
        using = true;
    }

    public void skillCooldown() {
        using = false;
        new Thread(() -> {
            cdTimeLeft = cooldownTime - 1;
            while (true) {
                wait(1000);
                cdTimeLeft--;
                if (cdTimeLeft == 0) {
                    available = true;
                    break;
                }
            }
            
        }).start();
    }

    public void useCooldown(int useTime){
        using = true;
        new Thread(() -> {
            useTimeLeft = useTime - 1;
            while (true) {
                wait(1000);
                useTimeLeft--;
                if (useTimeLeft == 0) {
                    skillCooldown();
                    break;
                }
            }
        }).start();
    }

    private void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setUsing(boolean using) {
        this.using = using;
    }

    public boolean isUsing() {
        return using;
    }

    public int getCdTimeLeft() {
        return cdTimeLeft;
    }

    public int getUseTimeLeft() {
        return useTimeLeft;
    }

    public void setUseTimeLeft(int useTimeLeft) {
        this.useTimeLeft = useTimeLeft;
    }
    
}