VAGRANT_VERSION = "2"

BOX_NAME = "raring64"
BOX_URL = "http://cloud-images.ubuntu.com/vagrant/raring/current/raring-server-cloudimg-amd64-vagrant-disk1.box"

Vagrant.configure(VAGRANT_VERSION) do |config|
  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = BOX_NAME

  # The url from where the 'config.vm.box' box will be fetched if it
  # doesn't already exist on the user's system.
  config.vm.box_url = BOX_URL

  # Create a forwarded port mapping which allows access to a specific port
  # within the machine from a port on the host machine. In the example below,
  # accessing "localhost:8081" will access port 80 on the guest machine.
  config.vm.network :forwarded_port, guest: 80, host: 8081
  config.vm.network :forwarded_port, guest: 8080, host: 8082
  config.vm.network :forwarded_port, guest: 3000, host: 3001

  # Create a private network, which allows host-only access to the machine
  # using a specific IP. This IP is used in the example Ansible inventory file.
  config.vm.network :private_network, ip: "192.168.222.112"

  # For information on available options for Ansible provisioning, please visit:
  # http://docs.vagrantup.com/v2/provisioning/ansible.html
  config.vm.provision :ansible do |ansible|
    ansible.playbook = "infrastructure/playbook.yml"
    ansible.inventory_path = "infrastructure/vm"
    ansible.verbose = true
  end

  # For information on available options for the Virtualbox provider, please visit:
  # http://docs.vagrantup.com/v2/virtualbox/configuration.html
  config.vm.provider :virtualbox do |vb|
    vb.customize ["modifyvm", :id, "--memory", "2048"]
  end
end
