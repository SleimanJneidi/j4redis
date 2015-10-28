package core;

/**
 * @author Sleiman
 *         <p/>
 *         Date: 16/10/15.
 */
public enum Commands implements Command {

    GET {
        private final byte[]cmd = {'g','e','t',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },

    SET {
        private final byte[]cmd = {'s','e','t',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },

    PING{
        private final byte[]cmd = {'P','I','N','G'};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },

    INCR{
        private final byte[]cmd = {'i','n','c','r',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },

    DECR{
        private final byte[]cmd = {'d','e','c','r',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },
    EXISTS{
        private final byte[]cmd = {'e','x','i','s','t','s',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },
    DEL{
        private final byte[]cmd = {'d','e','l',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },
    INCR_BY{
        private final byte[]cmd = {'i','n','c','r','b','y',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },
    DECR_BY{
        private final byte[]cmd = {'d','e','c','r','b','y',' '};
        @Override
        public byte[] getBytesPrefix() {
            return cmd;
        }
    },
    LPUSH{
        private final byte[]lpush = {'l','p','u','s','h',' '};
        @Override
        public byte[] getBytesPrefix() {
            return lpush;
        }
    },
    LRANGE{
        private final byte[]lrange = {'l','r','a','n','g','e',' '};
        @Override
        public byte[] getBytesPrefix() {
            return lrange;
        }
    },
    L_LENGTH{
        private final byte[]llength = {'l','l','e','n',' '};
        @Override
        public byte[] getBytesPrefix() {
            return llength;
        }
    },

    L_POP{
        private final byte[]lpop = {'l','p','o','p',' '};
        @Override
        public byte[] getBytesPrefix() {
            return lpop;
        }
    },

    L_INDEX{
        private final byte[]lindex = {'l','i','n','d','e','x',' '};
        @Override
        public byte[] getBytesPrefix() {
            return lindex;
        }
    },

    RPUSH{
        private final byte[]rpush = {'r','p','u','s','h',' '};
        @Override
        public byte[] getBytesPrefix() {
            return rpush;
        }
    },

    R_PUSH_X{
        private final byte[]rpushx = {'r','p','u','s','h','x',' '};
        @Override
        public byte[] getBytesPrefix() {
            return rpushx;
        }
    }
}
