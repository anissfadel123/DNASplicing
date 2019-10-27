/*------------------------------------------------------------------------------
 * Aniss Fadel
 * CISC 4900
 * Assignment 6: DNA Splicing
 * http://nifty.stanford.edu/2009/astrachan-dna/
 *------------------------------------------------------------------------------*/

public class LinkStrand implements IDnaStrand{
    private Node myFirst, myLast;
    private long mySize;
    public static SimpleStrand EMPTYSTRAND = new SimpleStrand();

    // Node of the linked list for storing genetic/DNA information
    public class Node{
        String info;
        Node next;

        Node(String dna){
            info = dna;
            next = null;
        }
    }

    /**
     * Create a strand representing an empty DNA strand, length of zero.
     */
    public LinkStrand() {
        this("");
    }

    /**
     * Create a strand representing s. No error checking is done to see if s represents
     * valid genomic/DNA data.
     * @param s is the source of cgat data for this strand
     */
    public LinkStrand(String s){
        myFirst = new Node(s);
        myLast = myFirst;
        mySize = s.length();
    }
    /**
     * Perform a cut with the enzyme as the source of the location for the cut. The strand
     * before the enzyme-cut becomes this strand (if no enzyme found, no cut is made). The strand
     * after the enzyme-cut is returned as a newly created Strand by this method. The first
     * occurrence of enzyme is the source of the cut being made by this method.
     * @param enzyme is the enzyme/genome at which a cut is made.
     */
    @Override
    public IDnaStrand cutWith(String enzyme) {
        int pos = myFirst.info.indexOf(enzyme);
        String myInfo = myFirst.info;
        if(pos == -1)
            return EMPTYSTRAND;
        IDnaStrand ret =
                new LinkStrand(myInfo.substring(pos + enzyme.length()));
        initializeFrom(myInfo.substring(0, pos));
        return ret;
    }

    @Override
    public IDnaStrand cutAndSplice(String enzyme, String splicee) {

        if(myFirst.next != null){
            throw new RuntimeException("link strand has more than one node");
        }

        int pos = 0;
        int lastPos = -1;
        String search = myFirst.info;
        boolean first = true;
        LinkStrand newHeadStrand = null;
        while ((pos = search.indexOf(enzyme, pos)) >= 0) {

            if (first) {
                // initialize the LinkStrand that will be returned
                first = false;
                newHeadStrand = new LinkStrand(search.substring(0, pos));
            } else {
                // add to nodes of the returned LinkStrand
                newHeadStrand.append(search.substring(lastPos, pos));
            }
            // add splicee to the nodes of the returned LinkStrand
            newHeadStrand.append(splicee);
            lastPos = pos+enzyme.length();
            pos++;
        }

        if (lastPos < search.length() && lastPos >= 0) {
            newHeadStrand.append(search.substring(lastPos));
        }

        // lastPos = -1 when no enzyme is found.
        if(lastPos == -1){
            newHeadStrand = new LinkStrand("");
        }
        return newHeadStrand;


    }

    @Override
    public long size() {
        return mySize;
    }

    @Override
    public void initializeFrom(String source) {
        myFirst = new Node(source);
        myLast = myFirst;
        mySize = source.length();
    }

    @Override
    public String strandInfo() {
        return this.getClass().getName();
    }



    @Override
    public IDnaStrand append(IDnaStrand dna) {
        if(dna instanceof LinkStrand){
            LinkStrand linkStrand = (LinkStrand) dna;
            myLast.next = linkStrand.myFirst;
            myLast = linkStrand.myLast;
            mySize += dna.size();

            return this;
        }
        return append(dna.toString());
    }

    @Override
    public IDnaStrand append(String dna) {
        myLast.next = new Node(dna);
        myLast = myLast.next;
        mySize += dna.length();
        return this;
    }

    @Override
    public String toString() {
        String s = myFirst.info;
        Node node = myFirst.next;
        while(node != null){
            s += node.info;
            node = node.next;
        }
        return s;
    }
}
