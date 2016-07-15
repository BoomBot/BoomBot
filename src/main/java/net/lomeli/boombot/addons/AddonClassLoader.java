package net.lomeli.boombot.addons;

import com.google.common.collect.Lists;

import java.io.File;
import java.net.*;
import java.util.List;

public class AddonClassLoader extends URLClassLoader {
    private URLLoaderWrapper mainClassLoader;
    private List<File> sources;

    public AddonClassLoader(ClassLoader parent) {
        super(parent instanceof URLClassLoader ? ((URLClassLoader) parent).getURLs() : new URL[]{}, null);
        this.mainClassLoader = new URLLoaderWrapper((URLClassLoader) parent);
        this.sources = Lists.newArrayList();
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        try {
            return this.mainClassLoader.loadClass(name);
        } catch (ClassNotFoundException ex) {
            return super.loadClass(name);
        }
    }

    public File[] getParentSources() {
        try {
            List<File> files = Lists.newArrayList();
            for (URL u : mainClassLoader.getSources()) {
                URI uri = u.toURI();
                if (uri.getScheme().equals("file")) files.add(new File(uri));
            }
            return files.toArray(new File[]{});
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return new File[0];
        }
    }

    public class URLLoaderWrapper extends URLClassLoader {
        private List<URL> sources;

        public URLLoaderWrapper(URLClassLoader loader) {
            super(loader.getURLs(), loader.getParent());
            this.sources = Lists.newArrayList();
            for (URL url : loader.getURLs())
                this.sources.add(url);
        }

        public void addURL(URL url) {
            super.addURL(url);
            this.sources.add(url);
        }

        public List<URL> getSources() {
            return sources;
        }
    }
}
