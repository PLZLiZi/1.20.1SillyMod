package plz.lizi.sillyyouare.sillymc;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.LivingEntity;

public class SillyEntityData extends SynchedEntityData {
    private final NonpublicObj objt = new NonpublicObj(this);

    public SillyEntityData(SynchedEntityData old) {
        super(old.entity);
        this.itemsById = old.itemsById;
    }

    public <T> T get(EntityDataAccessor<T> p_135371_) {
        if (p_135371_ == LivingEntity.DATA_HEALTH_ID){
            return (T)(Float)20.0F;
        }
        return super.get(p_135371_);
    }
}
