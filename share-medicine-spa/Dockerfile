FROM node:18-alpine


RUN addgroup -g 10014 choreo && \
    adduser  --disabled-password  --no-create-home --uid 10014 --ingroup choreo choreouser
USER 10014

WORKDIR /app


ENV HOST="0.0.0.0"
ENV DISABLE_DEV_SERVER_HOST_CHECK=true
ENV HTTPS=false

COPY . .



RUN npm install
EXPOSE 3000
CMD ["npm", "start"]