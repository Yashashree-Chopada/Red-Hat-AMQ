#include <proton/connection.hpp>
#include <proton/container.hpp>
#include <proton/message.hpp>
#include <proton/messaging_handler.hpp>
#include <proton/message_id.hpp>
#include <proton/sender.hpp>
#include <proton/sender_options.hpp>
#include <proton/target.hpp>
#include <proton/target_options.hpp>
#include <proton/timestamp.hpp>
#include <proton/connection_options.hpp>
#include <proton/ssl.hpp>
#include <proton/uuid.hpp>

#include <iostream>
#include <string>

class queue_browser : public proton::messaging_handler {
    std::string broker_url_;
    std::string queue_;
    std::string user_;
    std::string password_;

public:
    queue_browser(const std::string& url, const std::string& queue,
                  const std::string& user, const std::string& password)
        : broker_url_(url), queue_(queue), user_(user), password_(password) {}

    void on_container_start(proton::container& cont) override {
        proton::connection_options opts;
        opts.user(user_);
        if (!password_.empty()) {
            opts.password(password_);
        }
        
        // Allow insecure SASL mechanisms
        opts.sasl_allow_insecure_mechs(true);

        proton::connection conn = cont.connect(broker_url_, opts);
        conn.open_sender(queue_);
    }

    void on_sendable(proton::sender& sender) override {
        proton::message msg("Hello, AMQP with credentials!");
        sender.send(msg);
        std::cout << "Message sent.\n";
        sender.close();
        sender.connection().close();
    }
};

int main() {
    try {
        queue_browser sender("amqp://localhost:61616", "my_queue", "admin", "admin");
        proton::container(sender).run();
    } catch (const std::exception& e) {
        std::cerr << "ERROR: " << e.what() << "\n";
        return 1;
    }
    return 0;
}

