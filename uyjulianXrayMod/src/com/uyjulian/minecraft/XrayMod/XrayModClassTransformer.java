package com.uyjulian.minecraft.XrayMod;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import com.mumfrey.liteloader.core.transformers.Callback;
import com.mumfrey.liteloader.core.transformers.CallbackInjectionTransformer;
import com.mumfrey.liteloader.core.transformers.Callback.CallBackType;

import static org.objectweb.asm.Opcodes.*;
import net.minecraft.launchwrapper.IClassTransformer;

public class XrayModClassTransformer extends CallbackInjectionTransformer {

	@Override
	protected void addMappings() {
		// TODO Auto-generated method stub
		this.addCallbackMapping("net.minecraft.block.Block", "shouldSideBeRendered", "(Lnet/minecraft/world/IBlockAccess;IIII)Z", CallBackType.RETURN, new Callback("shouldSideBeRendered", "com.uyjulian.minecraft.UyjuliansXrayModMain", true));
		this.addCallbackMapping("net.minecraft.block.Block", "func_149646_a", "(Lnet/minecraft/world/IBlockAccess;IIII)Z", CallBackType.RETURN, new Callback("shouldSideBeRendered", "com.uyjulian.minecraft.UyjuliansXrayModMain", true));
		this.addCallbackMapping("ahu", "a", "(Lafx;IIII)Z", CallBackType.RETURN, new Callback("shouldSideBeRendered", "com.uyjulian.minecraft.UyjuliansXrayModMain", true));
	}
	
//	//TODO Obf 1.6.4
//	private static final String blockName = "net.minecraft.src.Block";
//	private static final String blockNameObf = "aqz";
//	private static final String iBlockAccessName = "net/minecraft/src/IBlockAccess";
//	private static final String iBlockAccessNameObf = "acf";
//	private static final String blockIDFieldName = "blockID";
//	private static final String blockIDFieldNameObf = "acf";
//	private static final String blockSideRenderMethodName = "shouldSideBeRendered";
//	private static final String blockSideRenderMethodSig = "(L" + iBlockAccessName + ";IIII)Z";
//	private static final String blockSideRenderMethodNameObf = "a";
//	private static final String blockSideRenderMethodSigObf = "(L" + iBlockAccessNameObf + ";IIII)Z";
//	
//	public XrayModClassTransformer() {
//		
//	}
//
//	@Override
//	public byte[] transform(String name, String arg1, byte[] basicClass) {
//		if (blockName.equals(name) || blockNameObf.equals(name))
//		{
//			return this.transformBlockClass(basicClass);
//		}
//		
//		return null;
//	}
//	
//	private byte[] transformBlockClass(byte[] basicClass) {
//		ClassNode classNode = this.readClass(basicClass);
//		for (MethodNode method : classNode.methods)
//		{
//			if ((blockSideRenderMethodName.equals(method.name) && blockSideRenderMethodSig.equals(method.desc)) || (blockSideRenderMethodNameObf.equals(method.name) && blockSideRenderMethodSigObf.equals(method.desc)))
//			{
//				this.transformBlockSideRenderMethod(classNode, method, blockSideRenderMethodNameObf.equals(method.name));
//			}
//		}
//		return this.writeClass(classNode);
//	}
//	
//	private void transformBlockSideRenderMethod(ClassNode classNode, MethodNode method, boolean obf) {
//		String iBlockAccessSelectionName = (obf ? iBlockAccessNameObf : iBlockAccessName);
//		String blockIDFieldSelectionName = (obf ? blockIDFieldNameObf : blockIDFieldName);
//		method.instructions.clear();
//		method.instructions.add(new VarInsnNode(ALOAD, 1));
//		method.instructions.add(new VarInsnNode(ILOAD, 2));
//		method.instructions.add(new VarInsnNode(ILOAD, 3));
//		method.instructions.add(new VarInsnNode(ILOAD, 4));
//		method.instructions.add(new VarInsnNode(ILOAD, 5));
//		method.instructions.add(new VarInsnNode(ALOAD, 0));
//		method.instructions.add(new VarInsnNode(ALOAD, 0));
//		method.instructions.add(new FieldInsnNode(GETFIELD, classNode.name, blockIDFieldSelectionName, "I"));
//		method.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/uyjulian/minecraft/XrayMod/UyjuliansXrayModMain", "shouldBlockSideBeRendered", "(L" + iBlockAccessSelectionName + ";IIIIL" + classNode.name + ";I)Z"));
//		method.instructions.add(new InsnNode(IRETURN));
//		
//	}
//	
//	/**
//	 * @param basicClass
//	 * @return
//	 */
//	private ClassNode readClass(byte[] basicClass)
//	{
//		ClassReader classReader = new ClassReader(basicClass);
//		ClassNode classNode = new ClassNode();
//		classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
//		return classNode;
//	}
//
//	/**
//	 * @param classNode
//	 * @return
//	 */
//	private byte[] writeClass(ClassNode classNode)
//	{
//		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//		classNode.accept(writer);
//		return writer.toByteArray();
//	}

}
