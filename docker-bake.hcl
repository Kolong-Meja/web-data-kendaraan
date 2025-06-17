group "default" {
  targets = [ "datakendaraan-api", "datakendaraan-frontend" ]
}

target "datakendaraan-api" {
  context = "./api-v1"
  dockerfile = "Dockerfile"
  tags = [ "datakendaraan-api:latest" ]
}

target "datakendaraan-frontend" {
  context = "./frontend"
  dockerfile = "Dockerfile"
  tags = [ "datakendaraan-frontend:latest" ]
}