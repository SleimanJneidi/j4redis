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
    }
}
