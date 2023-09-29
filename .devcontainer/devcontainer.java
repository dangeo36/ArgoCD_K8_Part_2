{
	"name": "Petclinic",
	"dockerFile": "Dockerfile",
	"runArgs": [
		"--cap-add=SYS_PTRACE",
		"--security-opt",
		"seccomp=unconfined",
		"--mount",
		"type=bind,source=${env:HOME}/.m2,target=/home/vscode/.m2",
		"--mount",
		"type=bind,source=${env:HOME}/.gradle,target=/home/vscode/.gradle",
		"--env",
		"GRADLE_USER_HOME=/home/vscode/.gradle"
	],
	"initializeCommand": "mkdir -p ${env:HOME}/.m2 ${env:HOME}/.gradle",
	"postCreateCommand": "sudo chown vscode:vscode /home/vscode/.m2 /home/vscode/.gradle",
	"remoteUser": "vscode",
	"features": {
		"docker-in-docker": "latest"
	},
	"extensions": [
		"vscjava.vscode-java-pack",
		"redhat.vscode-xml",
		"vmware.vscode-boot-dev-pack",
		"mhutchie.git-graph"
	],
	"forwardPorts": [8080],
	"settings": {
		"java.import.gradle.enabled": false,
		"java.server.launchMode": "Standard"
	}
}
