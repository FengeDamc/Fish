package fun.inject.inject.asm.transformers;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.event.events.EventStrafe;
import fun.inject.Agent;
import fun.inject.inject.Mappings;
import fun.inject.inject.asm.api.Inject;
import fun.inject.inject.asm.api.Transformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;

public class EntityTransformer extends Transformer {
    public EntityTransformer() {
        super("net/minecraft/entity/Entity");
    }
    @Inject(method = "func_70060_a",descriptor = "(FFF)V")
    public void onMoveFly(MethodNode methodNode) {
        InsnList list = new InsnList();
        //Agent.logger.info("moveFlying");
        int j =0;
        list.add(new VarInsnNode(Opcodes.ALOAD,0));

        list.add(new VarInsnNode(Opcodes.FLOAD,1));
        list.add(new VarInsnNode(Opcodes.FLOAD,2));
        list.add(new VarInsnNode(Opcodes.FLOAD,3));

        list.add(new VarInsnNode(Opcodes.ALOAD,0));
        //FD: pk/s net/minecraft/entity/Entity/field_70165_t
        //FD: pk/t net/minecraft/entity/Entity/field_70163_u
        //FD: pk/u net/minecraft/entity/Entity/field_70161_v
        //FD: pk/y net/minecraft/entity/Entity/field_70177_z
        //FD: pk/z net/minecraft/entity/Entity/field_70125_A
        list.add(new FieldInsnNode(Opcodes.GETFIELD,Mappings.getObfClass("net/minecraft/entity/Entity"), Mappings.getObfField("field_70177_z"),"F"));
        //event参数

        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EntityTransformer.class),"onStrafe","(Ljava/lang/Object;FFFF)Lcom/darkmagician6/eventapi/event/events/EventStrafe;"));
        list.add(new VarInsnNode(Opcodes.ASTORE,4));
        j++;
        list.add(new VarInsnNode(Opcodes.ALOAD,4));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/darkmagician6/eventapi/event/events/EventStrafe","strafe","F"));

        list.add(new VarInsnNode(Opcodes.FSTORE,1));

        list.add(new VarInsnNode(Opcodes.ALOAD,4));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/darkmagician6/eventapi/event/events/EventStrafe","forward","F"));

        list.add(new VarInsnNode(Opcodes.FSTORE,2));

        list.add(new VarInsnNode(Opcodes.ALOAD,4));
        list.add(new FieldInsnNode(Opcodes.GETFIELD,"com/darkmagician6/eventapi/event/events/EventStrafe","friction","F"));
        list.add(new VarInsnNode(Opcodes.FSTORE,3));

        ArrayList<AbstractInsnNode> rl=new ArrayList<>();
        //ArrayList<AbstractInsnNode> rload=new ArrayList<>();
        for (int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode node = methodNode.instructions.get(i);
            if(node instanceof VarInsnNode&&((VarInsnNode) node).var>=3+j){
                ((VarInsnNode) node).var+=j;//插入偏移值;
            }
            if(node instanceof FieldInsnNode&&((FieldInsnNode) node).name.equals(Mappings.getObfField("field_70177_z"))){
                 //标记替换yaw轴
                AbstractInsnNode aload_0 = methodNode.instructions.get(i-1);
                if(aload_0 instanceof VarInsnNode){
                    ((VarInsnNode) aload_0).var=4;
                    rl.add(node);
                }

            }

        }
        methodNode.instructions.insert(list);
        int bound = rl.size();
        for (int x = 0; x < bound; x++) {
                AbstractInsnNode node = rl.get(x);
                methodNode.instructions.insert(node, new FieldInsnNode(Opcodes.GETFIELD, "com/darkmagician6/eventapi/event/events/EventStrafe", "yaw", "F"));
                methodNode.instructions.remove(node);
        }





    }
    public static EventStrafe onStrafe(Object entity,float f0, float f1, float f2, float f){
        EventStrafe eventStrafe=new EventStrafe(f1,f0,f,f2);
        if(entity.getClass().getName().equals(Mappings.getObfClass("net/minecraft/client/entity/EntityPlayerSP").replace('/','.'))) EventManager.call(eventStrafe);
        return eventStrafe;
    }
    /*
       public void a(float var1, float var2, float var3) {
        float var4 = var1 * var1 + var2 * var2;
        if (!(var4 < 1.0E-4F)) {
            var4 = ns.c(var4);
            if (var4 < 1.0F) {
                var4 = 1.0F;
            }

            var4 = var3 / var4;
            var1 *= var4;
            var2 *= var4;
            float var5 = ns.a(this.y * 3.1415927F / 180.0F);
            float var6 = ns.b(this.y * 3.1415927F / 180.0F);
            this.v += (double)(var1 * var6 - var2 * var5);
            this.x += (double)(var2 * var6 + var1 * var5);
        }
    }

     */
}
