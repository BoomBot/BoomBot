package net.lomeli.boombot.core.addon;

import com.google.common.collect.Lists;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class AddonClassLoader extends URLClassLoader {
    private URLLoaderWrapper mainWrapper;
    private List<File> sources;

    public AddonClassLoader(ClassLoader parent) {
        super(parent instanceof URLClassLoader ? ((URLClassLoader) parent).getURLs() : new URL[0], null);
        this.mainWrapper = new URLLoaderWrapper(URLClassLoader.newInstance(this.getURLs(), parent));
        this.sources = Lists.newArrayList();
    }

    @Override
    public Class<?> loadClass(String s) throws ClassNotFoundException {
        try {
            return this.mainWrapper.loadClass(s);
        } catch (ClassNotFoundException ex) {
            return super.loadClass(s);
        }
    }

    public File[] getParentSources() {
        try {
            List<File> files = Lists.newArrayList();
            for (URL u : mainWrapper.getSources()) {
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
            this.sources = Lists.newArrayList(loader.getURLs());
        }

        @Override
        protected void addURL(URL url) {
            super.addURL(url);
            this.sources.add(url);
        }

        public List<URL> getSources() {
            return sources;
        }
    }
}
