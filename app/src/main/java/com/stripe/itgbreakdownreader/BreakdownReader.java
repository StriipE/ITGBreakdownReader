package com.stripe.itgbreakdownreader;

/**
 * Created by Ewen Auffret on 20/09/2017.
 */

public class BreakdownReader {

    private String[] blocks;
    private static int streamIndex = -1;

    public BreakdownReader(String breakdown)
    {
        blocks = readBreakdown(breakdown);
    }

    private String[] readBreakdown(String breakdown)
    {
        return breakdown.split(" ");
    }

    public static int getNextMesures(String block)
    {
        return Integer.parseInt(block);
    }

    public static boolean isBreak(String block)
    {
        return (block.charAt(0) == '(');
    }

    public static int getBreakMesures(String block)
    {
        block = block.replaceAll("[()]", "");
        return Integer.parseInt(block);
    }

    public String getNextBlock()
    {
        streamIndex++;
        return blocks[streamIndex];
    }

    public boolean isEndOfSong()
    {
        return (streamIndex == blocks.length - 1);
    }

    public String[] getBlocks()
    {
        return this.blocks;
    }
}
