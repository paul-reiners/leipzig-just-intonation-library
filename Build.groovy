import java.io.File

class Build {

	srcDir = "src"
	buildDir = "build"
	buildProdDir = buildDir + File.separator + "prod"
	vendorLibDir = "vendor" + File.separator + "lib"
	
	ant = new AntBuilder();
	
	void clean() {
		ant.delete(dir: buildDir)
	}
	
	void prepare() {
		clean()
		ant.mkdir(dir: buildProdDir)
	}
	
	void compile() {
		prepare()
		ant.javac(srcdir: srcDir,
		          destdir: buildProdDir,
		          classpath: projectClasspath(),
		          { compilerarg(line: "-source 1.4") })
	}
	
	String projectClasspath() {
		ant.path {
			fileset(dir: vendorLibDir) {
				include(name: "**/*.jar")
			}
		}
	}

  	static void main(args) {
  		new Build().compile()
  	}
}
